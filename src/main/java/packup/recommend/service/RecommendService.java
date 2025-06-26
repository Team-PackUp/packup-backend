package packup.recommend.service;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.stereotype.Service;
import packup.recommend.domain.builder.JpaDataModelBuilder;
import packup.recommend.domain.Recommend;
import packup.recommend.domain.repository.RecommendRepository;
import packup.recommend.dto.RecommendResponse;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository preferenceRepository;
    private final TourInfoRepository tourInfoRepository;

    // 협업 필터링 - 아이템 기반 추천
    // 기준 - 전체 회원이 평가한 데이터
    // 사용자가 선호한 상품과 유사한 상품을 추천(언센터드 코사인 - 코사인 유사도와 거의 비슷하지만, 벡터 평균을 빼지 않음.. 좀 더 빠른듯)
    public List<RecommendResponse> recommendForUser(long userId, Integer count)
            throws TasteException {

        // ───── 1) 추천 로직 그대로 ─────
        List<Recommend> preferences = preferenceRepository.findAll();
        DataModel model = JpaDataModelBuilder.buildPreferences(preferences);

        ItemSimilarity similarity = new UncenteredCosineSimilarity(model);          // 언센터드 코사인유사도(평균을 빼지 않음 + 점수 그대로 반영)
//        ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);      // 피어슨 상관계수(사용자별 점수를 보정하여 점수 도출)
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

        // 추천 상품 뽑기
        List<RecommendedItem> recommendList = recommender.recommend(userId, count, rescorer());

        System.out.println(userId);
        System.out.println(count);
        System.out.println(recommendList.size());

        // 미리 투어들을 한꺼번에 조회해 N+1 방지
        List<Long> tourIds = recommendList.stream()
                .map(RecommendedItem::getItemID)
                .toList();

        System.out.println(tourIds.size());

        Map<Long, TourInfo> tourMap = tourInfoRepository
                .findAllById(tourIds)
                .stream()
                .collect(Collectors.toMap(TourInfo::getSeq, Function.identity()));

        // DTO 변환
        List<RecommendResponse> recommendResponseList = recommendList.stream()
                .map(r -> {
                    TourInfo tour = tourMap.get(r.getItemID());
                    return RecommendResponse.builder()
                            .tour(TourInfoResponse.from(tour))
                            .build();
                })
                .toList();

        System.out.println(recommendResponseList.size());

        // 셔플·자르기
        return shuffleAndLimit(recommendResponseList);
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


    private IDRescorer rescorer() {
        return new IDRescorer() {
            @Override
            public double rescore(long id, double originalScore) {
                return originalScore;
            }

            @Override
            public boolean isFiltered(long id) {
                return false;
            }
        };
    }

}
