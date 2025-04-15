package packup.auth.infra.oauth.google.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import packup.auth.domain.OAuth2MemberClient;
import packup.auth.domain.OAuth2ServerType;
import packup.auth.infra.oauth.google.dto.GoogleMemberResponse;
import packup.user.domain.UserInfo;

@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OAuth2MemberClient {

    private final GoogleApiClient googleApiClient;

    @Override
    public OAuth2ServerType supportServer() {
        return OAuth2ServerType.GOOGLE;
    }

    @Override
    public UserInfo fetch(String accessToken) {
        GoogleMemberResponse response = googleApiClient.fetchMember("Bearer " + accessToken);
        return response.toDomain();
    }
}
