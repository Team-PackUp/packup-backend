package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour_booking")
@EntityListeners(AuditingEntityListener.class)
public class TourBooking {

    /** 투어 세션 예약 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("투어 세션 예약 식별번호")
    private Long seq;

    /** 투어 세션 (FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_session_seq", nullable = false)
    @Comment("투어 세션 식별번호")
    private TourSession tourSession;

    /** 예약자 (FK: USER_INFO) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq", nullable = false)
    @Comment("유저 식별번호")
    private UserInfo user;

    /** 결제 식별번호 */
    @Column(name = "payment_seq")
    @Comment("결제 식별번호")
    private Long paymentSeq;

    /** 예약 상태 코드 (int8 → BIGINT) */
    @Column(name = "booking_status_code")
    @Comment("예약 상태 코드")
    private Long bookingStatusCode;

    /** 성인 인원 수 (SMALLINT) */
    @Column(name = "booking_adult_count")
    @Comment("성인 인원 수")
    private Integer bookingAdultCount;

    /** 미성년자 인원 수 (SMALLINT) */
    @Column(name = "booking_kids_count")
    @Comment("미성년자 인원 수")
    private Integer bookingKidsCount;

    /** 프라이빗 예약 여부 */
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_private_flag", columnDefinition = "public.yn_enum")
    @Comment("프라이빗 예약 여부")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Builder.Default
    private YnType bookingPrivateFlag = YnType.N;

    /** 취소 약관 확인 여부 */
    @Enumerated(EnumType.STRING)
    @Column(name = "cancel_terms_checked_flag", columnDefinition = "public.yn_enum")
    @Comment("취소 약관 확인 여부")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Builder.Default
    private YnType cancelTermsCheckedFlag = YnType.N;

    /** 취소 신청 일시 */
    @Column(name = "cancel_requested_at")
    @Comment("취소 신청 일시")
    private LocalDateTime cancelRequestedAt;

    /** 취소 사유 */
    @Column(name = "cancel_reason", length = 255)
    @Comment("취소 사유")
    private String cancelReason;

    /** 취소 완료 일시 */
    @Column(name = "canceled_at")
    @Comment("취소 완료 일시")
    private LocalDateTime canceledAt;

    /** 체크인 일시 */
    @Column(name = "checked_in_at")
    @Comment("체크인 일시")
    private LocalDateTime checkedInAt;

    /** 노쇼 여부 */
    @Enumerated(EnumType.STRING)
    @Column(name = "no_show_flag", columnDefinition = "public.yn_enum")
    @Comment("노쇼 여부")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Builder.Default
    private YnType noShowFlag = YnType.N;

    /** 환불 결제 식별번호 */
    @Column(name = "refund_payment_seq")
    @Comment("환불 결제 식별번호")
    private Long refundPaymentSeq;

    /** 등록일시 */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Comment("등록일시")
    private LocalDateTime createdAt;

    /** 수정일시 */
    @LastModifiedDate
    @Column(name = "updated_at")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    /* ===================== 도메인 메서드 ===================== */

    /** 인원 및 프라이빗 여부 변경 */
    public void updateHeadcount(Integer adult, Integer kids, YnType privateFlag) {
        this.bookingAdultCount = adult;
        this.bookingKidsCount = kids;
        this.bookingPrivateFlag = privateFlag;
    }

    /** 상태 코드 변경 */
    public void changeStatus(Long statusCode) {
        this.bookingStatusCode = statusCode;
    }

    /** 결제/환불 연결 */
    public void attachPayment(Long paymentSeq) {
        this.paymentSeq = paymentSeq;
    }

    public void attachRefundPayment(Long refundPaymentSeq) {
        this.refundPaymentSeq = refundPaymentSeq;
    }

    /** 취소 요청/완료 */
    public void requestCancel(String reason, LocalDateTime requestedAt) {
        this.cancelReason = reason;
        this.cancelRequestedAt = requestedAt;
    }

    public void completeCancel(LocalDateTime canceledAt, Long canceledStatusCode) {
        this.canceledAt = canceledAt;
        this.bookingStatusCode = canceledStatusCode;
    }

    /** 체크인/노쇼 처리 */
    public void checkIn(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
        this.noShowFlag = YnType.N;
    }

    public void markNoShow(LocalDateTime when) {
        this.noShowFlag = YnType.Y;
        // 필요 시 checkedInAt 초기화 등 추가 처리
    }
}
