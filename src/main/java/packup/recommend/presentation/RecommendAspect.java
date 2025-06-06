package packup.recommend.presentation;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import packup.common.domain.repository.CommonCodeRepository;
import packup.recommend.annotation.RecommendTrace;
import packup.recommend.domain.RecommendScore;
import packup.recommend.domain.repository.RecommendScoreRepository;
import packup.recommend.dto.RecommendResponse;

@Aspect
@Component
@AllArgsConstructor
public class RecommendAspect {

    private ApplicationEventPublisher publisher;
    private RecommendScoreRepository recommendScoreRepository;
    private CommonCodeRepository commonCodeRepository;

    @AfterReturning(value = "@annotation(recommendTrace)", returning = "result")
    public void afterAction(JoinPoint joinPoint, RecommendTrace recommendTrace, Object result) {
        Object[] args = joinPoint.getArgs();

        Long userSeq = (Long) args[0];
        Long tourSeq = (Long) args[1];

        String actionTypeName = recommendTrace.actionType().getString();
        String actionType = commonCodeRepository.findByCodeName(
                        actionTypeName)
                .orElseThrow()
                .getCodeId();

        RecommendScore recommendScore = recommendScoreRepository.findByActionType(actionType);


        RecommendResponse recommendEvent = RecommendResponse.builder()
                .userSeq(userSeq)
                .tourSeq(tourSeq)
                .actionType(actionType)
                .score(recommendScore.getScore())
                .build();

        publisher.publishEvent(recommendEvent);
    }
}
