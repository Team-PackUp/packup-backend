package packup.auth.infra.oauth.kakao.client;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import packup.auth.infra.oauth.kakao.dto.KakaoMemberResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface KakaoApiClient {
    @GetExchange(url = "https://kapi.kakao.com/v2/user/me")
    KakaoMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String accessToken);
}
