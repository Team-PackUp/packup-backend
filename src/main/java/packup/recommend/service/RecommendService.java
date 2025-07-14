package packup.recommend.service;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import packup.common.dto.PageDTO;
import packup.recommend.domain.Recommend;
import packup.recommend.domain.builder.JpaDataModelBuilder;
import packup.recommend.domain.repository.RecommendRepository;
import packup.recommend.dto.RecommendResponse;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static packup.recommend.constant.RecommendConstant.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final TourInfoRepository tourInfoRepository;

    // 협업 필터링 - 아이템 기반 추천
    // 기준 - 전체 회원이 평가한 데이터
    // 사용자가 선호한 상품과 유사한 상품을 추천(언센터드 코사인 - 코사인 유사도와 거의 비슷하지만, 벡터 평균을 빼지 않음.. 좀 더 빠른듯)
    public PageDTO<TourInfoResponse> recommendForUser(long userId, int count, int page) throws TasteException {
        Page<TourInfoResponse> resultPage;

        GenericItemBasedRecommender recommender = generateRecommend();

        if (recommender == null) {
            resultPage = new PageImpl<>(
                    Collections.emptyList(),
                    PageRequest.of(page, PAGE_SIZE),
                    0
            );
            return PageDTO.of(resultPage);
        }

        List<RecommendedItem> recommendList;

        try {
            recommendList = recommender.recommend(userId, count, rescorer(LocalDate.now()));
        } catch (TasteException e) {
            resultPage = new PageImpl<>(
                    Collections.emptyList(),
                    PageRequest.of(page, PAGE_SIZE),
                    0
            );
            return PageDTO.of(resultPage);
        }

        if (recommendList.isEmpty()) {
            resultPage = new PageImpl<>(
                    Collections.emptyList(),
                    PageRequest.of(page, PAGE_SIZE),
                    0
            );
            return PageDTO.of(resultPage);
        }

        List<Long> tourIds = recommendList.stream()
                .map(RecommendedItem::getItemID)
                .toList();

        Map<Long, TourInfo> tourMap = tourInfoRepository.findAllById(tourIds)
                .stream()
                .collect(Collectors.toMap(TourInfo::getSeq, Function.identity()));

        List<TourInfoResponse> content = recommendList.stream()
                .map(r -> TourInfoResponse.from(tourMap.get(r.getItemID())))
                .toList();

        resultPage = new PageImpl<>(
                content,
                PageRequest.of(page, PAGE_SIZE),
                content.size()
        );

        return PageDTO.of(resultPage);
    }

    private GenericItemBasedRecommender generateRecommend() throws TasteException {
        List<Recommend> preferences = recommendRepository.findAll();
        if(preferences.isEmpty()) {
            return null;
        }
        DataModel model = JpaDataModelBuilder.buildPreferences(preferences);

        ItemSimilarity similarity = new UncenteredCosineSimilarity(model);

        return new GenericItemBasedRecommender(model, similarity);
    }


    public List<RecommendResponse> ensureMinimumRecommendation(List<RecommendResponse> recommendList, int needToAdd) {
        // 부족한 개수 데이터를 랜덤으로 투어 테이블에서 가져오기

        return recommendList;
    }

    private List<RecommendResponse> shuffleAndLimit(List<RecommendResponse> recommendList) {
        List<RecommendResponse> copyList = new ArrayList<>(recommendList);
        Collections.shuffle(copyList);

        return copyList;
    }

    // 2. Rescorer 구현을 DB 필터 버전으로 교체
    private IDRescorer rescorer(LocalDate today) {

        Set<Long> tourSeq = new HashSet<>(tourInfoRepository.findAllBetweenApplyDate(today));

        return new IDRescorer() {
            @Override public double rescore(long id, double original) {
                return original;                // 점수는 그대로
            }
            @Override public boolean isFiltered(long id) {
                return !tourSeq.contains(id);  // today 범위를 벗어나면 필터링
            }
        };
    }

}
