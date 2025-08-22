package packup.guide.dto.guideInfo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.dto.UserInfoResponse;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideInfoResponse {

    private Long seq;
    private UserInfoResponse user;

    private String guideIdcardImageUrl;
    private JsonNode guideLanguage;
    private String guideIntroduce;

    private YnType termsAgreedFlag;
    private LocalDateTime termsAgreedAt;

    private JsonNode serviceItemsChecked;
    private LocalDateTime serviceItemsCheckedAt;

    private YnType suspensionFlag;
    private String suspensionReason;
    private Long suspensionAdminSeq;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GuideInfoResponse from(GuideInfo guideInfo) {
        UserInfo user = guideInfo.getUser();
        UserDetailInfo userDetail = user.getDetailInfo();

        UserInfoResponse userDto = UserInfoResponse.builder()
                .email(user.getEmail())
                .gender(userDetail.getGender())
                .birth(userDetail.getBirth())
                .nation(userDetail.getNation())
                .joinType(user.getJoinType())
                .language(userDetail.getLanguage())
                .nickname(userDetail.getNickname())
                .profileImagePath(userDetail.getProfileImagePath())
                .pushFlag(userDetail.getPushFlag())
                .build();

        return GuideInfoResponse.builder()
                .seq(guideInfo.getSeq())
                .user(userDto)
                .guideIdcardImageUrl(guideInfo.getGuideIdcardImageUrl())
                .guideLanguage(guideInfo.getGuideLanguage())
                .guideIntroduce(guideInfo.getGuideIntroduce())
                .termsAgreedFlag(guideInfo.getTermsAgreedFlag())
                .termsAgreedAt(guideInfo.getTermsAgreedAt())
                .serviceItemsChecked(guideInfo.getServiceItemsChecked())
                .serviceItemsCheckedAt(guideInfo.getServiceItemsCheckedAt())
                .suspensionFlag(guideInfo.getSuspensionFlag())
                .suspensionReason(guideInfo.getSuspensionReason())
                .suspensionAdminSeq(guideInfo.getSuspensionAdminSeq())
                .createdAt(guideInfo.getCreatedAt())
                .updatedAt(guideInfo.getUpdatedAt())
                .build();
    }
}
