package packup.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.auth.domain.OAuth2MemberClientComposite;
import packup.auth.domain.OAuth2ServerType;
import packup.auth.dto.OAuth2LoginRequest;
import packup.auth.dto.OAuth2LoginResponse;
import packup.config.security.provider.JwtTokenProvider;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final OAuth2MemberClientComposite clientComposite;
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2LoginResponse login(String providerName, OAuth2LoginRequest loginRequest) {
        OAuth2ServerType provider = OAuth2ServerType.fromName(providerName);

        UserInfo userInfo = clientComposite.fetch(provider, loginRequest.getAccessToken());

        UserInfo savedUserInfo = userInfoRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userInfoRepository.save(userInfo));

        String jwt = jwtTokenProvider.createToken(String.valueOf(savedUserInfo.getSeq()));

        return new OAuth2LoginResponse(jwt);
    }
}
