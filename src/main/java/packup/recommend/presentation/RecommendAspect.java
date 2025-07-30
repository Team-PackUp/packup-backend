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
import packup.recommend.dto.RecommendRequest;
import packup.recommend.dto.RecommendResponse;
import packup.recommend.enums.ActionType;
import packup.reply.dto.ReplyRequest;

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

        RecommendRequest recommendRequest = buildRecommend(args, recommendTrace);
        if(recommendRequest.getScore() == 0) return;

        publisher.publishEvent(buildRecommend(args, recommendTrace));
    }

    private RecommendRequest buildRecommend(Object[] args, RecommendTrace recommendTrace) {
        float score;
        Long tourSeq;

        ActionType actionTypeEnum = recommendTrace.actionType();
        String actionTypeName = recommendTrace.actionType().getString();

        Long userSeq = (Long) args[0];

        String actionType = commonCodeRepository.findByCodeName(
                        actionTypeName)
                .orElseThrow()
                .getCodeId();
        RecommendScore recommendScore = recommendScoreRepository.findByActionType(actionType);

        switch (actionTypeEnum) {

            // 리뷰에서 2점 이하는 부정적이라 판단하여 점수에서 제외
            case REVIEW :
                ReplyRequest req = (ReplyRequest) args[1];
                tourSeq = req.getTargetSeq();
                score = req.getPoint();
                if(score <= 2) score = 0;
                break;

            default:
                tourSeq = (Long) args[1];
                score = recommendScore.getScore();
                break;
        }

        return RecommendRequest.builder()
                .userSeq(userSeq)
                .tourSeq(tourSeq)
                .actionType(actionType)
                .score(score)
                .build();
    }

}
