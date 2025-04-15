package packup.auth.domain;

import org.springframework.stereotype.Component;
import packup.auth.exception.AuthException;
import packup.auth.exception.AuthExceptionType;
import packup.user.domain.UserInfo;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class OAuth2MemberClientComposite {

    private final Map<OAuth2ServerType, OAuth2MemberClient> clients;

    public OAuth2MemberClientComposite(Set<OAuth2MemberClient> clients) {
        this.clients = clients.stream()
                .collect(toMap(OAuth2MemberClient::supportServer, identity()));
    }

    public UserInfo fetch(OAuth2ServerType oauthServerType, String authCode) {
        return getClient(oauthServerType).fetch(authCode);
    }


    private OAuth2MemberClient getClient(OAuth2ServerType oauthServerType) {
        return Optional.ofNullable(clients.get(oauthServerType))
                .orElseThrow(() -> new AuthException(AuthExceptionType.UNSUPPORTED_OAUTH_TYPE));
    }
}
