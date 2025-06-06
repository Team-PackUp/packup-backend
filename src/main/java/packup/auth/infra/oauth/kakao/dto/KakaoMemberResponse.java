package packup.auth.infra.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import packup.common.enums.YnType;
import packup.common.util.UUIDGenerator;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;

import java.time.LocalDateTime;


@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoMemberResponse(
        Long id,
        boolean hasSignedUp,
        LocalDateTime connectedAt,
        KakaoAccount kakaoAccount
) {

    public UserInfo toDomain(String joinTypeCodeId) {
        UserDetailInfo userDetailInfo = UserDetailInfo.builder()
                .profileImagePath(kakaoAccount.profile.profileImageUrl)
                .nickname(UUIDGenerator.generate(kakaoAccount.profile.nickname))
                .build();

        UserPrefer userPrefer = UserPrefer.builder().build();

        UserInfo userInfo = UserInfo.builder()
                .email(kakaoAccount.email)
                .prefer(userPrefer)
                .joinType(joinTypeCodeId)
                .detailInfo(userDetailInfo)
                .banFlag(YnType.N)
                .adultFlag(YnType.Y)
                .withdrawFlag(YnType.N)
                .build();

        userInfo.addDetailInfo(userDetailInfo);

        return userInfo;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KakaoAccount(
            boolean profileNeedsAgreement,
            boolean profileNicknameNeedsAgreement,
            boolean profileImageNeedsAgreement,
            Profile profile,
            boolean nameNeedsAgreement,
            String name,
            boolean emailNeedsAgreement,
            boolean isEmailValid,
            boolean isEmailVerified,
            String email,
            boolean ageRangeNeedsAgreement,
            String ageRange,
            boolean birthyearNeedsAgreement,
            String birthyear,
            boolean birthdayNeedsAgreement,
            String birthday,
            String birthdayType,
            boolean genderNeedsAgreement,
            String gender,
            boolean phoneNumberNeedsAgreement,
            String phoneNumber,
            boolean ciNeedsAgreement,
            String ci,
            LocalDateTime ciAuthenticatedAt
    ) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Profile(
            String nickname,
            String thumbnailImageUrl,
            String profileImageUrl,
            boolean isDefaultImage
    ) {
    }
}