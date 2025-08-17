package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourBooking;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingCheckInRequest implements TourBookingUpdateRequest {

    @NotNull(message = "checkedInAt은 필수입니다.")
    private LocalDateTime checkedInAt;

    @Override
    public void applyTo(TourBooking entity) {
        entity.checkIn(checkedInAt);
    }
}
