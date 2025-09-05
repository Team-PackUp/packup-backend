package packup.tour.dto.tourSession;

import lombok.*;
import packup.tour.domain.TourSession;

import java.time.LocalDateTime;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TourSessionResponse {
    private Long seq;
    private Long tourSeq;
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;
    private Integer sessionStatusCode;
    private LocalDateTime cancelledAt;

    public static TourSessionResponse from(TourSession s) {
        return TourSessionResponse.builder()
                .seq(s.getSeq())
                .tourSeq(s.getTour().getSeq())
                .sessionStartTime(s.getSessionStartTime())
                .sessionEndTime(s.getSessionEndTime())
                .sessionStatusCode(s.getSessionStatusCode())
                .cancelledAt(s.getCancelledAt())
                .build();
    }
}
