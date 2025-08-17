package packup.guide.domain;

import jakarta.persistence.*;
        import lombok.*;
        import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

/**
 * GUIDE_APPLICATION (가이드 지원서)
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "guide_application")
@EntityListeners(AuditingEntityListener.class)
public class GuideApplication {

    /** 가이드 지원서 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("가이드 지원서 식별번호")
    private Long seq;

    /** 유저 식별번호 (FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq", nullable = false)
    @Comment("유저 식별번호")
    private UserInfo user;

    /** 가이드 신분증 이미지 URL */
    @Column(name = "guide_idcard_image_url", columnDefinition = "TEXT")
    @Comment("가이드 신분증 이미지 URL")
    private String guideIdcardImageUrl;

    /** 가이드 소개 */
    @Column(name = "guide_introduce", columnDefinition = "TEXT")
    @Comment("가이드 소개")
    private String guideIntroduce;

    /** 지원서 제출일시 */
    // 컬럼명이 'application_registed_at' (오타 포함) 이므로 그대로 매핑
    @Column(name = "application_registed_at")
    @Comment("지원서 제출일시")
    private LocalDateTime applicationRegisteredAt;

    /** 지원서 상태 (정수 코드) */
    @Column(name = "application_status_code")
    @Comment("지원서 상태")
    private Integer applicationStatusCode;

    /** 지원서 처리 관리자 식별번호 */
    @Column(name = "application_admin_seq")
    @Comment("지원서 처리 관리자 식별번호")
    private Long applicationAdminSeq;

    /** 반려 사유 */
    @Column(name = "reject_reason", columnDefinition = "TEXT")
    @Comment("반려 사유")
    private String rejectReason;

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

    /** 지원자 측 내용 수정 */
    public void updateApplicant(String guideIdcardImageUrl, String guideIntroduce) {
        this.guideIdcardImageUrl = guideIdcardImageUrl;
        this.guideIntroduce = guideIntroduce;
    }

    /** 관리자 처리(상태/반려사유/담당자) */
    public void updateByAdmin(Integer applicationStatusCode, Long applicationAdminSeq, String rejectReason) {
        this.applicationStatusCode = applicationStatusCode;
        this.applicationAdminSeq = applicationAdminSeq;
        this.rejectReason = rejectReason;
    }
}
