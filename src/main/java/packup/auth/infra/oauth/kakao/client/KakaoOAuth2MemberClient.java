package packup.auth.infra.oauth.kakao.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import packup.auth.domain.OAuth2MemberClient;
import packup.auth.domain.OAuth2ServerType;
import packup.auth.exception.AuthException;
import packup.auth.exception.AuthExceptionType;
import packup.auth.infra.oauth.google.client.GoogleApiClient;
import packup.auth.infra.oauth.google.dto.GoogleMemberResponse;
import packup.auth.infra.oauth.kakao.dto.KakaoMemberResponse;
import packup.common.domain.repository.CommonCodeRepository;
import packup.user.domain.UserInfo;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2MemberClient implements OAuth2MemberClient {

    private final KakaoApiClient kakaoApiClient;
    private final CommonCodeRepository commonCodeRepository;

    @Override
    public OAuth2ServerType supportServer() {
        return OAuth2ServerType.KAKAO;
    }

    @Override
    public UserInfo fetch(String accessToken) {
        KakaoMemberResponse response = kakaoApiClient.fetchMember("Bearer " + accessToken);

        String joinTypeCodeId = commonCodeRepository.findByCodeName("KAKAO")
                .orElseThrow(() -> new AuthException(AuthExceptionType.INVALID_OAUTH_TYPE_CODE))
                .getCodeId();

        return response.toDomain(joinTypeCodeId);
    }
}
