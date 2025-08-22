package packup.tour.dto.tourBooking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.tour.domain.TourBooking;
import packup.tour.domain.TourSession;
import packup.user.domain.UserInfo;

/**
 * TourBooking 생성 요청 DTO
 * - createdAt/updatedAt 은 Auditing 처리
 * - seq 는 자동 생성
 * - bookingPrivateFlag, cancelTermsCheckedFlag 는 엔티티 기본값을 사용할 수 있으나,
 *   비즈니스 정책에 따라 cancelTermsCheckedFlag 는 필수로 받도록 설정했습니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourBookingCreateRequest {

    /** 투어 세션 식별번호 (FK) */
    @NotNull(message = "tourSessionSeq는 필수입니다.")
    private Long tourSessionSeq;

    /** 예약자 식별번호 (FK: USER_INFO) */
    @NotNull(message = "userSeq는 필수입니다.")
    private Long userSeq;

    /** 성인 인원 수 (0 이상) */
    @NotNull(message = "bookingAdultCount는 필수입니다.")
    @PositiveOrZero(message = "bookingAdultCount는 0 이상이어야 합니다.")
    private Integer bookingAdultCount;

    /** 미성년자 인원 수 (0 이상, 선택) */
    @Min(value = 0, message = "bookingKidsCount는 0 이상이어야 합니다.")
    private Integer bookingKidsCount;

    /** 프라이빗 예약 여부 (선택, 미전송 시 엔티티 기본값 N 사용) */
    private YnType bookingPrivateFlag;

    /** 취소 약관 확인 여부 (정책상 필수 수신) */
    @NotNull(message = "cancelTermsCheckedFlag는 필수입니다.")
    private YnType cancelTermsCheckedFlag;

    /** 초기 예약 상태 코드 (선택, 서비스 정책에 따라 설정) */
    private Long bookingStatusCode;

    /**
     * 사전에 조회한 연관 엔티티로 신규 TourBooking 엔티티를 생성합니다.
     *
     * @param tourSession 조회된 TourSession 엔티티 (필수)
     * @param user        조회된 UserInfo 엔티티 (필수)
     */
    public TourBooking toEntity(TourSession tourSession, UserInfo user) {
        TourBooking.TourBookingBuilder builder = TourBooking.builder()
                .tourSession(tourSession)
                .user(user)
                .bookingAdultCount(this.bookingAdultCount)
                .bookingKidsCount(this.bookingKidsCount)
                .cancelTermsCheckedFlag(this.cancelTermsCheckedFlag);

        if (this.bookingPrivateFlag != null) {
            builder.bookingPrivateFlag(this.bookingPrivateFlag);
        }
        if (this.bookingStatusCode != null) {
            builder.bookingStatusCode(this.bookingStatusCode);
        }

        return builder.build();
    }
}
