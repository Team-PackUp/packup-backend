package packup.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.auth.dto.OAuth2LoginRequest;
import packup.auth.dto.OAuth2LoginResponse;
import packup.auth.service.OAuth2Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuth2ApiController {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/login/{provider}")
    public OAuth2LoginResponse login(
            @PathVariable String provider,
            @RequestBody OAuth2LoginRequest loginRequest
    ) {
        return oAuth2Service.login(provider, loginRequest);
    }

}
