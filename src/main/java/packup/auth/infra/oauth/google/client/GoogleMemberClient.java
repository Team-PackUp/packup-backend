package packup.auth.infra.oauth.google.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import packup.auth.domain.OAuth2MemberClient;
import packup.auth.domain.OAuth2ServerType;
import packup.auth.exception.AuthException;
import packup.auth.exception.AuthExceptionType;
import packup.auth.infra.oauth.google.dto.GoogleMemberResponse;
import packup.common.domain.repository.CommonCodeRepository;
import packup.user.domain.UserInfo;

@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OAuth2MemberClient {

    private final GoogleApiClient googleApiClient;
    private final CommonCodeRepository commonCodeRepository;

    @Override
    public OAuth2ServerType supportServer() {
        return OAuth2ServerType.GOOGLE;
    }

    @Override
    public UserInfo fetch(String accessToken) {
        GoogleMemberResponse response = googleApiClient.fetchMember("Bearer " + accessToken);

        String joinTypeCodeId = commonCodeRepository.findByCodeName("GOOGLE")
                .orElseThrow(() -> new AuthException(AuthExceptionType.INVALID_OAUTH_TYPE_CODE))
                .getCodeId();

        return response.toDomain(joinTypeCodeId);
    }
}
