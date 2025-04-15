package packup.auth.infra.oauth.google.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import packup.auth.infra.oauth.google.dto.GoogleMemberResponse;

public interface GoogleOAuth2MemberClient {

    @GetExchange("https://www.googleapis.com/oauth2/v2/userinfo")
    GoogleMemberResponse fetchMember(@RequestHeader(AUTHORIZATION) String bearerToken);

}
