package packup.tour.dto.tourBooking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.tour.domain.TourBooking;
import packup.tour.dto.tourSession.TourSessionResponse;
import packup.user.dto.UserInfoResponse;

import java.time.LocalDateTime;

/**
 * TourBooking 응답 DTO
 * - 연관관계는 ID(tourSessionSeq, userSeq)만 노출
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourBookingResponse {

    /** 투어 세션 예약 식별번호 (PK) */
    private Long seq;

    /** 투어 세션 식별번호 (FK) */
    private TourSessionResponse tourSession;

    /** 예약자 식별번호 (FK: USER_INFO) */
    private UserInfoResponse user;

    /** 결제/환불 식별번호 */
    private Long paymentSeq;
    private Long refundPaymentSeq;

    /** 예약 상태 코드 */
    private Long bookingStatusCode;

    /** 인원 수 */
    private Integer bookingAdultCount;
    private Integer bookingKidsCount;

    /** 프라이빗 예약 여부 */
    private YnType bookingPrivateFlag;

    /** 취소 약관 확인 여부 */
    private YnType cancelTermsCheckedFlag;

    /** 취소 관련 */
    private LocalDateTime cancelRequestedAt;
    private String cancelReason;
    private LocalDateTime canceledAt;

    /** 체크인/노쇼 */
    private LocalDateTime checkedInAt;
    private YnType noShowFlag;

    /** 감사 필드 */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 엔티티 → DTO 매핑 */
    public static TourBookingResponse from(TourBooking tourBooking) {
        return TourBookingResponse.builder()
                .seq(tourBooking.getSeq())
                .tourSession(TourSessionResponse.from(tourBooking.getTourSession()))
                .user(UserInfoResponse.of(tourBooking.getUser()))
                .paymentSeq(tourBooking.getPaymentSeq())
                .refundPaymentSeq(tourBooking.getRefundPaymentSeq())
                .bookingStatusCode(tourBooking.getBookingStatusCode())
                .bookingAdultCount(tourBooking.getBookingAdultCount())
                .bookingKidsCount(tourBooking.getBookingKidsCount())
                .bookingPrivateFlag(tourBooking.getBookingPrivateFlag())
                .cancelTermsCheckedFlag(tourBooking.getCancelTermsCheckedFlag())
                .cancelRequestedAt(tourBooking.getCancelRequestedAt())
                .cancelReason(tourBooking.getCancelReason())
                .canceledAt(tourBooking.getCanceledAt())
                .checkedInAt(tourBooking.getCheckedInAt())
                .noShowFlag(tourBooking.getNoShowFlag())
                .createdAt(tourBooking.getCreatedAt())
                .updatedAt(tourBooking.getUpdatedAt())
                .build();
    }
}
