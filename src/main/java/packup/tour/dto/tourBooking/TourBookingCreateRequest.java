package packup.tour.dto.tourBooking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TourBookingCreateRequest {

    @NotNull(message = "tourSessionSeq는 필수입니다.")
    private Long tourSessionSeq;

    @NotNull
    @Min(1)
    private Integer bookingAdultCount;

    private Integer bookingKidsCount;

    @NotNull
    private String bookingPrivateFlag;

    @NotNull
    private String orderId;

    @NotNull
    private String paymentKey;

    @NotNull
    @Min(0)
    private Integer amount;
}
