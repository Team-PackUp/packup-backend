package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import packup.common.enums.YnType;
import packup.tour.domain.TourBooking;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingHeadcountUpdateRequest implements TourBookingUpdateRequest {

    @NotNull(message = "bookingAdultCount는 필수입니다.")
    @Min(value = 0, message = "bookingAdultCount는 0 이상이어야 합니다.")
    private Integer bookingAdultCount;

    @NotNull(message = "bookingKidsCount는 필수입니다.")
    @Min(value = 0, message = "bookingKidsCount는 0 이상이어야 합니다.")
    private Integer bookingKidsCount;

    @NotNull(message = "bookingPrivateFlag는 필수입니다.")
    private YnType bookingPrivateFlag;

    @Override
    public void applyTo(TourBooking entity) {
        entity.updateHeadcount(bookingAdultCount, bookingKidsCount, bookingPrivateFlag);
    }
}
