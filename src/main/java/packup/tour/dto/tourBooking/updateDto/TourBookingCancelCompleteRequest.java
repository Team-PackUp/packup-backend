package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourBooking;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingCancelCompleteRequest implements TourBookingUpdateRequest {

    @NotNull(message = "canceledAt은 필수입니다.")
    private LocalDateTime canceledAt;

    @NotNull(message = "canceledStatusCode는 필수입니다.")
    private Long canceledStatusCode;

    @Override
    public void applyTo(TourBooking entity) {
        entity.completeCancel(canceledAt, canceledStatusCode);
    }
}
