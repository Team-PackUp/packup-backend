package packup.recommend.presentation;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import packup.recommend.domain.Recommend;
import packup.recommend.domain.repository.RecommendRepository;
import packup.recommend.dto.RecommendRequest;
import packup.recommend.dto.RecommendResponse;

@Component
@AllArgsConstructor
public class RecommendListener {
    private final RecommendRepository recommendRepository;

    @EventListener
    public void recordScore(RecommendRequest event) {
        Recommend recommend = Recommend.of(
                event.getUserSeq(),
                event.getTourSeq(),
                event.getActionType(),
                event.getScore()
        );

        recommendRepository.save(recommend);
    }
}

