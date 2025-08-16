package packup.guide.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "guide_info")
public class GuideInfo {

    /** 가이드 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("가이드 식별번호")
    private Long seq;

    /** 유저 (고유 1:1) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    @Comment("유저 식별번호")
    private UserInfo user;

    /** 가이드 신분증 이미지 URL (TEXT) */
    @Column(name = "guide_idcard_image_url", columnDefinition = "TEXT")
    @Comment("가이드 신분증 이미지 URL")
    private String guideIdcardImageUrl;

    /** 가이드 언어 (jsonb) 예: ["ko","en"] */
    @Type(JsonBinaryType.class)
    @Column(name = "guide_language", columnDefinition = "jsonb")
    @Comment("가이드 언어(JSONB)")
    private JsonNode guideLanguage;

    /** 가이드 소개 (TEXT) */
    @Lob
    @Column(name = "guide_introduce")
    @Comment("가이드 소개")
    private String guideIntroduce;


    /** 활동약관 동의여부 (public.yn_enum) */
    @Enumerated(EnumType.STRING)
    @Column(name = "terms_agreed_flag", columnDefinition = "public.yn_enum", nullable = false)
    @Comment("활동약관 동의여부(Y/N)")
    @Builder.Default
    private YnType termsAgreedFlag = YnType.N;


    /** 활동약관 동의 일시 */
    @Column(name = "terms_agreed_at")
    @Comment("활동약관 동의 일시")
    private LocalDateTime termsAgreedAt;
    @Column(name = "guide_rating", columnDefinition = "smallint", nullable = false)
    private short guideRating = 0;  // 기본값 0

    /** 제공콘텐츠 확인 여부 (jsonb) */
    @Type(JsonBinaryType.class)
    @Column(name = "service_items_checked", columnDefinition = "jsonb")
    @Comment("제공콘텐츠 확인 여부(JSONB)")
    private JsonNode serviceItemsChecked;

    /** 제공콘텐츠 확인 일시 */
    @Column(name = "service_items_checked_at")
    @Comment("제공콘텐츠 확인 일시")
    private LocalDateTime serviceItemsCheckedAt;

    /** 권한 정지 여부 (public.yn_enum) */
    @Enumerated(EnumType.STRING)
    @Column(name = "suspension_flag", columnDefinition = "public.yn_enum", nullable = false)
    @Comment("권한 정지 여부(Y/N)")
    @Builder.Default
    private YnType suspensionFlag = YnType.N;

    /** 권한 정지 사유 */
    @Column(name = "suspension_reason", length = 255)
    @Comment("권한 정지 사유")
    private String suspensionReason;


    /** 권한 정지 관리자 식별번호 */
    @Column(name = "suspension_admin_seq")
    @Comment("권한 정지 관리자 식별번호")
    private Long suspensionAdminSeq;


    /** 등록일시 */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default current_timestamp")
    @Comment("등록일시")
    private LocalDateTime createdAt;

    /** 수정일시 */
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

}
