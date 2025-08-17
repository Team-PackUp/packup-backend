package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import packup.tour.domain.TourBooking;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingStatusUpdateRequest implements TourBookingUpdateRequest {

    @NotNull(message = "bookingStatusCode는 필수입니다.")
    private Long bookingStatusCode;

    @Override
    public void applyTo(TourBooking entity) {
        entity.changeStatus(bookingStatusCode);
    }
}
