package packup.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.auth.domain.OAuth2MemberClientComposite;
import packup.config.security.provider.JwtTokenProvider;
import packup.user.domain.repository.UserInfoRepository;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final OAuth2MemberClientComposite clientComposite;
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;


}
