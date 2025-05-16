package packup.fcmpush.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.fcmpush.dto.FcmTokenRequest;
import packup.fcmpush.service.FcmPushService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmPushController {

    private final FcmPushService notificationService;

    @PostMapping("/register")
    public ResultModel<Void> registerFcmToken(
            @Auth Long memberId,
            @RequestBody FcmTokenRequest request
    ) {
        notificationService.registerOrUpdateFcmToken(memberId, request.getFcmToken());
        return ResultModel.success();
    }

}
