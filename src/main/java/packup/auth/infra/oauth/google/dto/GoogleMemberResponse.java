package packup.auth.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import packup.auth.domain.OAuth2ServerType;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String name,
        String givenName,
        String picture,
        String locale
) {

    public UserInfo toDomain() {

        UserDetailInfo userDetailInfo = UserDetailInfo.builder()
                .profileImagePath(picture)
                .nickname(givenName)
                .build();

        UserInfo userInfo = UserInfo.builder()
                .joinType(String.valueOf(OAuth2ServerType.GOOGLE))
                .detailInfo(userDetailInfo)
                .build();

        userInfo.addDetailInfo(userDetailInfo);

        return userInfo;
    }
}
