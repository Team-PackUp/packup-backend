package packup.recommend.presentation;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;
import packup.recommend.dto.RecommendResponse;
import packup.recommend.exception.RecommendException;
import packup.recommend.service.RecommendService;
import packup.tour.dto.TourInfoResponse;

import java.util.List;

import static packup.recommend.exception.RecommendExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public ResultModel<RecommendResponse> recommendForUser(@Auth Long memberId, @RequestParam Integer count, @RequestParam Integer page) throws TasteException {

        if(count == null || count < 1) {
            throw new RecommendException(ABNORMAL_ACCESS);
        }

        // 알고리즘 적용
        PageDTO<TourInfoResponse> aiResponseList = recommendService.recommendForUser(memberId, count, page);
        
        // 추천 결과가 요청 갯수보다 적을 경우, DB에서 랜덤 상품 추가
//        if(recommendResponseList.size() < count) {
//            int needToAdd = count - recommendResponseList.size();
//            recommendResponseList = recommendationService.ensureMinimumRecommendation(recommendResponseList, needToAdd);
//        }

        RecommendResponse recommendResponse = RecommendResponse.builder()
                .tour(aiResponseList)
                .build();

        return ResultModel.success(recommendResponse);
    }

}

