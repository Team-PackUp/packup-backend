package packup.auth.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import packup.auth.domain.OAuth2MemberInfo;
import packup.auth.domain.OAuth2ServerType;
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
        return UserInfo.builder()
                .joinType(String.valueOf(OAuth2ServerType.GOOGLE))
                .detailInfo()
                .nickname(givenName)
                .profileImageUrl(picture)
                .build();
    }
}
