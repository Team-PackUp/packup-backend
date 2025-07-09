package packup.alert.presentation;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import packup.alert.domain.Alert;
import packup.alert.domain.repository.AlertRepository;
import packup.alert.dto.AlertRequest;

@Component
@AllArgsConstructor
public class AlertListener {
    private final AlertRepository alertRepository;

    @EventListener
    public void recordAlert(AlertRequest event) {
        Alert alert = Alert.of(
                event.getUserSeq(),
                event.getAlertType()
        );

        alertRepository.save(alert);

    }

    @EventListener
    public void pushFcm(AlertRequest event) {
        Alert alert = Alert.of(
                event.getUserSeq(),
                event.getAlertType()
        );

        alertRepository.save(alert);

    }
}
