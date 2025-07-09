package packup.alert.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class AlertResponse {
    private Long seq;
    private String alertType;
    private LocalDateTime createdAt;
}
