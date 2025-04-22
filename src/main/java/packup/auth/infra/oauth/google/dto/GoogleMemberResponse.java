package packup.auth.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import packup.auth.domain.OAuth2ServerType;
import packup.common.enums.YnType;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String name,
        String givenName,
        String picture,
        String locale,
        String email
) {

    public UserInfo toDomain() {

        UserDetailInfo userDetailInfo = UserDetailInfo.builder()
                .profileImagePath(picture)
                .nickname(givenName)
                .build();

        UserPrefer userPrefer = UserPrefer.builder()
                .build();

        UserInfo userInfo = UserInfo.builder()
                .email(email)
                .prefer(userPrefer)
                .joinType(String.valueOf(OAuth2ServerType.GOOGLE))
                .detailInfo(userDetailInfo)
                .banFlag(YnType.N)
                .adultFlag(YnType.Y)
                .withdrawFlag(YnType.N)
                .build();

        userInfo.addDetailInfo(userDetailInfo);

        return userInfo;
    }
}
