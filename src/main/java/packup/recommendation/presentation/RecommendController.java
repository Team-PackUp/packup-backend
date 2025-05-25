package packup.recommendation.presentation;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.recommendation.service.RecommendService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendationService;

    @GetMapping("/user")
    public List<String> recommendForUser(@Auth Long memberId, @RequestParam int count) throws TasteException {
        System.out.println("추천 요청한 사용자 ID: " + memberId);

        List<RecommendedItem> items = recommendationService.recommendForUser(memberId, count);

        for (RecommendedItem item : items) {
            System.out.println("상품 ID: " + item.getItemID() + ", 유사도 포인트: " + item.getValue());
        }

        return items.stream()
                .map(i -> "상품 ID: " + i.getItemID() + ", 유사도 점수: " + i.getValue())
                .collect(Collectors.toList());
    }

}

