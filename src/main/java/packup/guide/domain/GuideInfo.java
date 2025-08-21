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
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "guide_info")
public class GuideInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("가이드 식별번호")
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    @Comment("유저 식별번호")
    private UserInfo user;

    @Column(name = "guide_idcard_image_url", columnDefinition = "TEXT")
    @Comment("가이드 신분증 이미지 URL")
    private String guideIdcardImageUrl;

    @Type(JsonBinaryType.class)
    @Column(name = "guide_language", columnDefinition = "jsonb")
    @Comment("가이드 언어(JSONB)")
    private JsonNode guideLanguage;

    @Lob
    @Column(name = "guide_introduce")
    @Comment("가이드 소개(자유 서술)")
    private String guideIntroduce;

    // ====== 신규 필드들 ======

    @Column(name = "years", columnDefinition = "smallint", nullable = false)
    @Comment("가이드 활동 연차")
    private short years;

    @Column(name = "role_summary", columnDefinition = "TEXT")
    @Comment("어떤 일을 하는지 요약(90자 이내 권장)")
    private String roleSummary;

    @Column(name = "expertise", columnDefinition = "TEXT")
    @Comment("전문성/자격 요약(90자 이내 권장)")
    private String expertise;

    @Column(name = "achievement", columnDefinition = "TEXT")
    @Comment("수상/언론/성취(선택, 90자 이내 권장)")
    private String achievement;

    @Column(name = "summary", columnDefinition = "TEXT")
    @Comment("최종 요약(90자 이내 권장)")
    private String summary;

    // ======================

    @Enumerated(EnumType.STRING)
    @Column(name = "terms_agreed_flag", columnDefinition = "public.yn_enum", nullable = false)
    @Builder.Default
    private YnType termsAgreedFlag = YnType.N;

    @Column(name = "terms_agreed_at")
    private LocalDateTime termsAgreedAt;

    @Column(name = "guide_rating", columnDefinition = "smallint", nullable = false)
    private short guideRating = 0;

    @Type(JsonBinaryType.class)
    @Column(name = "service_items_checked", columnDefinition = "jsonb")
    private JsonNode serviceItemsChecked;

    @Column(name = "service_items_checked_at")
    private LocalDateTime serviceItemsCheckedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "suspension_flag", columnDefinition = "public.yn_enum", nullable = false)
    @Builder.Default
    private YnType suspensionFlag = YnType.N;

    @Column(name = "suspension_reason", length = 255)
    private String suspensionReason;

    @Column(name = "suspension_admin_seq")
    private Long suspensionAdminSeq;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

    // 편의 메서드(업서트용)
    public GuideInfo applyIntro(short years, String roleSummary, String expertise,
                                String achievement, String summary) {
        this.years = years;
        this.roleSummary = roleSummary;
        this.expertise = expertise;
        this.achievement = achievement;
        this.summary = summary;
        return this;
    }
}
