package packup.auth.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import packup.auth.domain.OAuth2ServerType;
import packup.common.enums.YnType;
import packup.common.util.UUIDGenerator;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;

import java.util.UUID;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String name,
        String givenName,
        String picture,
        String locale,
        String email
) {

    public UserInfo toDomain(String joinTypeCodeId) {

        UserDetailInfo userDetailInfo = UserDetailInfo.builder()
                .profileImagePath(picture)
                .nickname(UUIDGenerator.generate(givenName))
                .build();

        UserPrefer userPrefer = UserPrefer.builder()
                .preferCategorySeqJson(null)
                .build();

        UserInfo userInfo = UserInfo.builder()
                .email(email)
                .prefer(userPrefer)
                .joinType(joinTypeCodeId)
                .detailInfo(userDetailInfo)
                .banFlag(YnType.N)
                .adultFlag(YnType.Y)
                .withdrawFlag(YnType.N)
                .build();

        userInfo.addDetailInfo(userDetailInfo);
        userInfo.addPrefer(userPrefer);

        return userInfo;
    }
}
