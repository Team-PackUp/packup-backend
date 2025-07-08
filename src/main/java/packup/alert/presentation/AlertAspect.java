package packup.alert.presentation;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import packup.alert.annotation.AlertTrace;
import packup.alert.dto.AlertRequest;
import packup.alert.enums.AlertType;
import packup.common.domain.repository.CommonCodeRepository;

@Aspect
@Component
@AllArgsConstructor
public class AlertAspect {
    private ApplicationEventPublisher publisher;
    private CommonCodeRepository commonCodeRepository;

    @AfterReturning(value = "@annotation(alertTrace)", returning = "result")
    public void afterAction(JoinPoint joinPoint, AlertTrace alertTrace, Object result) {
        Object[] args = joinPoint.getArgs();

        publisher.publishEvent(buildAlert(args, alertTrace));
    }

    // 제목 및 내용은 국제화 이슈도 있고 해서 프론트에서 처리 해야할듯
    private AlertRequest buildAlert(Object[] args, AlertTrace alertTrace) {

        AlertType alertTypeEnum = alertTrace.alertType();
        String alertTypeName = alertTypeEnum.getString();

        Long userSeq = (Long) args[0];

        String alertType = commonCodeRepository.findByCodeName(
                alertTypeName)
                .orElseThrow()
                .getCodeId();

        return AlertRequest.builder()
                .userSeq(userSeq)
                .alertType(alertType)
                .build();
    }
}
