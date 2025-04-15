package packup.auth.domain;

import packup.user.domain.UserInfo;

public interface OAuth2MemberClient {
    OAuth2ServerType supportServer();

    UserInfo fetch(String accessToken);
}
