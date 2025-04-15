package packup.auth.infra.oauth;

import packup.auth.domain.OAuth2MemberInfo;

public interface OAuth2MemberClient {
    OAuth2MemberInfo fetch(String accessToken);
}
