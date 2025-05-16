package packup.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.auth.dto.OAuth2LoginRequest;
import packup.auth.dto.OAuth2LoginResponse;
import packup.auth.dto.RefreshTokenRequest;
import packup.auth.dto.RefreshTokenResponse;
import packup.auth.service.OAuth2Service;
import packup.common.dto.ResultModel;
import packup.fcmpush.dto.FcmTokenRequest;
import packup.fcmpush.service.FcmPushService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuth2ApiController {

    private final OAuth2Service oAuth2Service;
    private final FcmPushService fcmPushService;

    @PostMapping("/login/{provider}")
    public ResultModel<OAuth2LoginResponse> login(
            @PathVariable String provider,
            @RequestBody OAuth2LoginRequest loginRequest
    ) {
        OAuth2LoginResponse oAuth2LoginResponse = oAuth2Service.login(provider, loginRequest);
        return ResultModel.success(oAuth2LoginResponse);
    }

    @PostMapping("/refresh")
    public ResultModel<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String accessToken = oAuth2Service.refresh(request.getRefreshToken());
        return ResultModel.success(new RefreshTokenResponse(accessToken));
    }

    @DeleteMapping("/logout")
    public ResultModel<Void> logout(@Auth Long userId, @RequestBody(required = false) FcmTokenRequest request) {
        oAuth2Service.logout(userId);

        if (hasValidFcmToken(request)) {
            fcmPushService.deactivateFcmToken(request.getFcmToken());
        }

        return ResultModel.success();
    }

    private boolean hasValidFcmToken(FcmTokenRequest request) {
        return request != null && request.getFcmToken() != null && !request.getFcmToken().isBlank();
    }

}

