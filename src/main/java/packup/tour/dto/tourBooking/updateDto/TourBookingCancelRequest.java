package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourBooking;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingCancelRequest implements TourBookingUpdateRequest {

    @NotBlank(message = "cancelReason은 필수입니다.")
    private String cancelReason;

    @NotNull(message = "cancelRequestedAt은 필수입니다.")
    private LocalDateTime cancelRequestedAt;

    @Override
    public void applyTo(TourBooking entity) {
        entity.requestCancel(cancelReason, cancelRequestedAt);
    }
}
