package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import packup.tour.domain.TourBooking;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingMarkNoShowRequest implements TourBookingUpdateRequest {

    @NotNull(message = "when은 필수입니다.")
    private LocalDateTime when;

    @Override
    public void applyTo(TourBooking entity) {
        entity.markNoShow(when);
    }
}
