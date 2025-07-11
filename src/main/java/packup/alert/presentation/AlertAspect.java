package packup.alert.presentation;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import packup.alert.annotation.AlertTrace;
import packup.alert.domain.Alert;
import packup.alert.domain.repository.AlertRepository;
import packup.alert.dto.AlertRequest;
import packup.alert.enums.AlertType;
import packup.common.domain.repository.CommonCodeRepository;

@Aspect
@Component
@AllArgsConstructor
public class AlertAspect {
    private final AlertRepository alertRepository;

    @AfterReturning(value = "@annotation(alertTrace)", returning = "result")
    public void afterAction(JoinPoint joinPoint, AlertTrace alertTrace, Object result) {
        Object[] args = joinPoint.getArgs();

        AlertRequest alertRequest = buildAlert(args, alertTrace);

        Alert alert = Alert.of(
                alertRequest.getUserSeq(),
                alertRequest.getAlertType()
        );

        alertRepository.save(alert);
    }

    private AlertRequest buildAlert(Object[] args, AlertTrace trace) {
        Long userSeq = (Long) args[0];
        AlertType type = trace.alertType();

        return AlertRequest.builder()
                .userSeq(userSeq)
                .alertType(type.code())
                .build();
    }
}
