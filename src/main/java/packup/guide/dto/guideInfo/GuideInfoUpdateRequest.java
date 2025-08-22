package packup.guide.dto.guideInfo;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;

import java.time.LocalDateTime;

/**
 * GuideInfo 업데이트 요청 DTO
 * - 연관관계(user)는 변경하지 않습니다.
 * - guideRating은 집계/운영 값이므로 여기서 수정하지 않습니다.
 * - null 필드는 무시(현재 값 유지)하며, 값이 있는 필드만 엔티티에 적용합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideInfoUpdateRequest {

    /** 가이드 신분증 이미지 URL */
    @Size(max = 2000, message = "guideIdcardImageUrl 길이가 너무 깁니다.")
    private String guideIdcardImageUrl;

    /** 가이드 언어(JSONB) 예: ["ko","en"] */
    private JsonNode guideLanguage;

    /** 가이드 소개 (Lob) */
    private String guideIntroduce;

    /** 활동약관 동의 여부 (Y/N) */
    private YnType termsAgreedFlag;

    /** 활동약관 동의 일시 (동의가 Y일 때 필수) */
    private LocalDateTime termsAgreedAt;

    /** 제공콘텐츠 확인 여부(JSONB) */
    private JsonNode serviceItemsChecked;

    /** 제공콘텐츠 확인 일시 */
    private LocalDateTime serviceItemsCheckedAt;

    /** 권한 정지 여부 (운영용) */
    private YnType suspensionFlag;

    /** 권한 정지 사유 (운영용) */
    @Size(max = 255, message = "suspensionReason은 255자 이하여야 합니다.")
    private String suspensionReason;

    /** 권한 정지 관리자 식별번호 (운영용) */
    private Long suspensionAdminSeq;

    /* ========= 일관성 검증 ========= */

    @AssertTrue(message = "termsAgreedFlag가 Y이면 termsAgreedAt이 필요합니다.")
    public boolean isTermsAgreedConsistency() {
        if (termsAgreedFlag == null) return true;
        return termsAgreedFlag == YnType.N || termsAgreedAt != null;
    }

    @AssertTrue(message = "suspensionFlag가 Y이면 suspensionReason과 suspensionAdminSeq가 필요합니다.")
    public boolean isSuspensionConsistency() {
        if (suspensionFlag == null || suspensionFlag == YnType.N) return true;
        return suspensionReason != null && !suspensionReason.isBlank() && suspensionAdminSeq != null;
    }

    /* ========= 엔티티 적용 ========= */

    /**
     * 전달된 값(비null)만 GuideInfo 엔티티에 반영합니다.
     * 트랜잭션 내에서 호출되면 더티체킹으로 DB에 반영됩니다.
     */
    public void applyTo(GuideInfo entity) {
        if (guideIdcardImageUrl != null) {
            entity.setGuideIdcardImageUrl(guideIdcardImageUrl);
        }
        if (guideLanguage != null) {
            entity.setGuideLanguage(guideLanguage);
        }
        if (guideIntroduce != null) {
            entity.setGuideIntroduce(guideIntroduce);
        }
        if (termsAgreedFlag != null) {
            entity.setTermsAgreedFlag(termsAgreedFlag);
        }
        if (termsAgreedAt != null) {
            entity.setTermsAgreedAt(termsAgreedAt);
        }
        if (serviceItemsChecked != null) {
            entity.setServiceItemsChecked(serviceItemsChecked);
        }
        if (serviceItemsCheckedAt != null) {
            entity.setServiceItemsCheckedAt(serviceItemsCheckedAt);
        }
        if (suspensionFlag != null) {
            entity.setSuspensionFlag(suspensionFlag);
        }
        if (suspensionReason != null) {
            entity.setSuspensionReason(suspensionReason);
        }
        if (suspensionAdminSeq != null) {
            entity.setSuspensionAdminSeq(suspensionAdminSeq);
        }
    }
}
