package packup.auth.infra.oauth.kakao.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import packup.auth.domain.OAuth2MemberClient;
import packup.auth.domain.OAuth2ServerType;
import packup.auth.infra.oauth.google.client.GoogleApiClient;
import packup.auth.infra.oauth.google.dto.GoogleMemberResponse;
import packup.auth.infra.oauth.kakao.dto.KakaoMemberResponse;
import packup.user.domain.UserInfo;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2MemberClient implements OAuth2MemberClient {

    private final GoogleApiClient googleApiClient;
    private final KakaoApiClient kakaoApiClient;

    @Override
    public OAuth2ServerType supportServer() {
        return OAuth2ServerType.KAKAO;
    }

    @Override
    public UserInfo fetch(String accessToken) {
        KakaoMemberResponse response = kakaoApiClient.fetchMember("Bearer " + accessToken);
        return response.toDomain();
    }
}
