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

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "guide_info")
public class GuideInfo {

    /** 가이드 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    @Comment("가이드 식별번호")
    private Long seq;

    /** 유저 (고유 1:1) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", referencedColumnName = "user_seq", nullable = false)
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
    @Builder.Default
    @Comment("활동약관 동의여부(Y/N)")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType termsAgreedFlag = YnType.N;


    /** 활동약관 동의 일시 */
    @Column(name = "terms_agreed_at", columnDefinition = "timestamptz")
    @Comment("활동약관 동의 일시")
    private LocalDateTime termsAgreedAt;
    @Column(name = "guide_rating", columnDefinition = "smallint", nullable = false)
    private short guideRating = 0;  // 기본값 0

    /** 제공콘텐츠 확인 여부 (jsonb) */
    @Type(JsonBinaryType.class)
    @Column(name = "service_items_checked", columnDefinition = "jsonb")
    @Comment("제공콘텐츠 확인 여부")
    private JsonNode serviceItemsChecked;

    /** 제공콘텐츠 확인 일시 */
    @Column(name = "service_items_checked_at", columnDefinition = "timestamp")
    @Comment("제공콘텐츠 확인 일시")
    private LocalDateTime serviceItemsCheckedAt;

    /** 권한 정지 여부 (public.yn_enum) */
    @Enumerated(EnumType.STRING)
    @Column(name = "suspension_flag", columnDefinition = "public.yn_enum")
    @Builder.Default
    @Comment("권한 정지 여부(Y/N)")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
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

    /** 활동 연차: int2(smallint) */
    @Column(name = "years", nullable = false, columnDefinition = "smallint DEFAULT 0")
    @Comment("가이드 활동 연차(년)")
    private short years;

    /** 어떤 일을 하시나요? 90자 권장 */
    @Column(name = "role_summary", columnDefinition = "TEXT")
    @Comment("역할 요약")
    private String roleSummary;

    /** 전문성/자격 요약 90자 권장 */
    @Column(name = "expertise", columnDefinition = "TEXT")
    @Comment("전문성 요약")
    private String expertise;

    /** 수상/언론 보도 이력(선택) 90자 권장 */
    @Column(name = "achievement", columnDefinition = "TEXT")
    @Comment("직업적 성취")
    private String achievement;

    /** 최종 요약 90자 권장 */
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
}
