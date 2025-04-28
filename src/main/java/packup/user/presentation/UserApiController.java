package packup.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.user.domain.UserInfo;
import packup.user.dto.UserInfoResponse;
import packup.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/me")
    public UserInfoResponse getMyInfo(@Auth Long memberId) {
        return userService.getUserInfo(memberId);
    }
}
