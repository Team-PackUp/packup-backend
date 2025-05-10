package packup.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.auth.domain.OAuth2MemberClientComposite;
import packup.auth.domain.OAuth2ServerType;
import packup.auth.domain.RefreshToken;
import packup.auth.domain.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;

    public OAuth2LoginResponse login(String providerName, OAuth2LoginRequest loginRequest) {
        OAuth2ServerType provider = OAuth2ServerType.fromName(providerName);

        UserInfo userInfo = clientComposite.fetch(provider, loginRequest.getAccessToken());

        UserInfo savedUserInfo = userInfoRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userInfoRepository.save(userInfo));

        Long userId = savedUserInfo.getSeq();

        String accessToken = jwtTokenProvider.createToken(String.valueOf(savedUserInfo.getSeq()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, jwtTokenProvider.getRefreshTokenExpiryDate()),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .userId(userId)
                                        .token(refreshToken)
                                        .expiresAt(jwtTokenProvider.getRefreshTokenExpiryDate())
                                        .build()
                        )
                );

        return new OAuth2LoginResponse(accessToken, refreshToken);
    }
}
