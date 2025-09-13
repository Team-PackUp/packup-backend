package packup.tour.dto.tourBooking;

import lombok.Builder;
import lombok.Getter;
import packup.common.enums.YnType;
import packup.tour.domain.TourBooking;

import java.time.LocalDateTime;

@Getter
@Builder
public class TourBookingResponse {
    private Long bookingSeq;
    private Long tourSessionSeq;
    private Long userSeq;
    private Integer adultCount;
    private Integer kidsCount;
    private YnType privateFlag;
    private Long paymentSeq;
    private Long bookingStatusCode;
    private LocalDateTime createdAt;

    public static TourBookingResponse from(TourBooking b) {
        return TourBookingResponse.builder()
                .bookingSeq(b.getSeq())
                .tourSessionSeq(b.getTourSession().getSeq())
                .userSeq(b.getUser().getSeq())
                .adultCount(b.getBookingAdultCount())
                .kidsCount(b.getBookingKidsCount())
                .privateFlag(b.getBookingPrivateFlag())
                .paymentSeq(b.getPaymentSeq())
                .bookingStatusCode(b.getBookingStatusCode())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
