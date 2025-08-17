package packup.tour.dto.tourBooking.updateDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourBooking;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TourBookingAttachPaymentRequest implements TourBookingUpdateRequest {

    @NotNull(message = "paymentSeq는 필수입니다.")
    private Long paymentSeq;

    @Override
    public void applyTo(TourBooking entity) {
        entity.attachPayment(paymentSeq);
    }
}
