package packup.guide.dto.guideInfo;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

/**
 * GuideInfo 생성 요청 DTO
 * - createdAt / updatedAt 은 엔티티에서 자동 처리 (Hibernate @CreationTimestamp/@UpdateTimestamp)
 * - rating/suspension 관련 필드는 생성 시 받지 않음(운영/집계로 관리)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideInfoCreateRequest {

    /** 유저 식별번호 (FK: USER_INFO) */
    @NotNull(message = "userSeq는 필수입니다.")
    private Long userSeq;

    /** 가이드 신분증 이미지 URL */
    @Size(max = 2000, message = "guideIdcardImageUrl 길이가 너무 깁니다.")
    private String guideIdcardImageUrl;

    /** 가이드 언어(JSONB) 예: ["ko","en"] */
    private JsonNode guideLanguage;

    /** 가이드 소개 (Lob) */
    private String guideIntroduce;

    /** 활동약관 동의 여부 (Y/N) */
    @NotNull(message = "termsAgreedFlag는 필수입니다.")
    private YnType termsAgreedFlag;

    /** 활동약관 동의 일시 (동의가 Y일 때 필수) */
    private LocalDateTime termsAgreedAt;

    /** 제공콘텐츠 확인 여부(JSONB) */
    private JsonNode serviceItemsChecked;

    /** 제공콘텐츠 확인 일시 */
    private LocalDateTime serviceItemsCheckedAt;

    /** 동의 여부/일시 일관성 검증 */
    @AssertTrue(message = "termsAgreedFlag가 Y이면 termsAgreedAt이 필요합니다.")
    public boolean isTermsAgreedConsistency() {
        if (termsAgreedFlag == null) return true;
        return termsAgreedFlag == YnType.N || termsAgreedAt != null;
    }

    /**
     * 사전에 조회한 UserInfo 엔티티를 받아 GuideInfo 엔티티로 변환
     */
    public GuideInfo toEntity(UserInfo user) {
        return GuideInfo.builder()
                .user(user)
                .guideIdcardImageUrl(this.guideIdcardImageUrl)
                .guideLanguage(this.guideLanguage)
                .guideIntroduce(this.guideIntroduce)
                .termsAgreedFlag(this.termsAgreedFlag)
                .termsAgreedAt(this.termsAgreedAt)
                // guideRating은 초기 0(엔티티 기본값) 사용
                .serviceItemsChecked(this.serviceItemsChecked)
                .serviceItemsCheckedAt(this.serviceItemsCheckedAt)
                // suspensionFlag/Reason/AdminSeq는 생성 시 미설정(운영 전용)
                .build();
    }
}
