package packup.alert.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class AlertRequest {
    private final Long  userSeq;
    private final String alertType;
}
