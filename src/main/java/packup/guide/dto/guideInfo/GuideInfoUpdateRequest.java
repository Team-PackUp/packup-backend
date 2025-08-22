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


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideInfoUpdateRequest {

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


    public void applyTo(GuideInfo entity) {
        entity.applyUpdate(this);
    }
}
