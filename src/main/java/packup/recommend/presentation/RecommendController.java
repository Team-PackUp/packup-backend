package packup.recommend.presentation;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.recommend.dto.RecommendResponse;
import packup.recommend.exception.RecommendException;
import packup.recommend.service.RecommendService;

import java.util.List;

import static packup.recommend.exception.RecommendExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendationService;

    @GetMapping("/user")
    public ResultModel<List<RecommendResponse>> recommendForUser(@Auth Long memberId, @RequestParam Integer count) throws TasteException {

        if(count == null || count < 1) {
            throw new RecommendException(ABNORMAL_ACCESS);
        }

        List<RecommendResponse> recommendResponseList = recommendationService.recommendForUser(memberId, count);

        // 추천 결과가 요청 갯수보다 적을 경우, DB에서 랜덤 상품 추가
        if(recommendResponseList.size() < count) {
            int needToAdd = count - recommendResponseList.size();
            recommendResponseList = recommendationService.ensureMinimumRecommendation(recommendResponseList, needToAdd);
        }

        return ResultModel.success(recommendResponseList);
    }

}

