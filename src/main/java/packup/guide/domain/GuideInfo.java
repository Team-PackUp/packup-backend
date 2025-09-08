package packup.guide.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;
import packup.guide.dto.guideInfo.GuideInfoUpdateRequest;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "guide_info")
public class GuideInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    @Comment("가이드 식별번호")
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    @Comment("유저 식별번호")
    private UserInfo user;

    @Column(name = "guide_idcard_image_url", columnDefinition = "TEXT")
    @Comment("가이드 신분증 이미지 URL")
    private String guideIdcardImageUrl;

    @Type(JsonBinaryType.class)
    @Column(name = "guide_language", columnDefinition = "jsonb")
    @Comment("가이드 언어(JSONB)")
    private JsonNode guideLanguage;

    @Column(name = "guide_introduce", columnDefinition = "TEXT")
    @Comment("가이드 소개")
    private String guideIntroduce;

    @Enumerated(EnumType.STRING)
    @Column(name = "terms_agreed_flag", columnDefinition = "public.yn_enum", nullable = false)
    @Builder.Default
    @Comment("활동약관 동의여부(Y/N)")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType termsAgreedFlag = YnType.N;

    @Column(name = "terms_agreed_at", columnDefinition = "timestamptz")
    @Comment("활동약관 동의 일시")
    private LocalDateTime termsAgreedAt;


    @Type(JsonBinaryType.class)
    @Column(name = "service_items_checked", columnDefinition = "jsonb")
    @Comment("제공콘텐츠 확인 여부")
    private JsonNode serviceItemsChecked;

    @Column(name = "service_items_checked_at", columnDefinition = "timestamp")
    @Comment("제공콘텐츠 확인 일시")
    private LocalDateTime serviceItemsCheckedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "suspension_flag", columnDefinition = "public.yn_enum")
    @Builder.Default
    @Comment("권한 정지 여부(Y/N)")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType suspensionFlag = YnType.N;

    @Column(name = "suspension_reason", length = 255)
    @Comment("권한 정지 사유")
    private String suspensionReason;

    @Column(name = "suspension_admin_seq")
    @Comment("권한 정지 관리자 식별번호")
    private Long suspensionAdminSeq;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default current_timestamp")
    @Comment("등록일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    @Column(name = "years", nullable = false, columnDefinition = "smallint DEFAULT 0")
    @Comment("가이드 활동 연차(년)")
    private short years;

    @Column(name = "role_summary", columnDefinition = "TEXT")
    @Comment("역할 요약")
    private String roleSummary;

    @Column(name = "expertise", columnDefinition = "TEXT")
    @Comment("전문성 요약")
    private String expertise;

    @Column(name = "achievement", columnDefinition = "TEXT")
    @Comment("직업적 성취")
    private String achievement;

    @Column(name = "summary", columnDefinition = "TEXT")
    @Comment("최종 요약")
    private String summary;

    public GuideInfo applyIntro(short years, String roleSummary, String expertise,
                                String achievement, String summary) {
        this.years = years;
        this.roleSummary = roleSummary;
        this.expertise = expertise;
        this.achievement = achievement;
        this.summary = summary;
        return this;
    }

    public void applyUpdate(GuideInfoUpdateRequest r) {
        if (r.getGuideIdcardImageUrl() != null) this.guideIdcardImageUrl = r.getGuideIdcardImageUrl();
        if (r.getGuideLanguage() != null) this.guideLanguage = r.getGuideLanguage();
        if (r.getGuideIntroduce() != null) this.guideIntroduce = r.getGuideIntroduce();

        if (r.getTermsAgreedFlag() != null) {
            this.termsAgreedFlag = r.getTermsAgreedFlag();
            if (this.termsAgreedFlag == YnType.N) {
                this.termsAgreedAt = null;
            } else if (r.getTermsAgreedAt() != null) {
                this.termsAgreedAt = r.getTermsAgreedAt();
            }
        } else if (r.getTermsAgreedAt() != null) {
            this.termsAgreedAt = r.getTermsAgreedAt();
        }

        if (r.getServiceItemsChecked() != null) this.serviceItemsChecked = r.getServiceItemsChecked();
        if (r.getServiceItemsCheckedAt() != null) this.serviceItemsCheckedAt = r.getServiceItemsCheckedAt();

        if (r.getSuspensionFlag() != null) {
            this.suspensionFlag = r.getSuspensionFlag();
            if (this.suspensionFlag == YnType.Y) {
                if (r.getSuspensionReason() != null) this.suspensionReason = r.getSuspensionReason();
                if (r.getSuspensionAdminSeq() != null) this.suspensionAdminSeq = r.getSuspensionAdminSeq();
            } else {
                this.suspensionReason = null;
                this.suspensionAdminSeq = null;
            }
        } else {
            if (r.getSuspensionReason() != null) this.suspensionReason = r.getSuspensionReason();
            if (r.getSuspensionAdminSeq() != null) this.suspensionAdminSeq = r.getSuspensionAdminSeq();
        }
    }
}
