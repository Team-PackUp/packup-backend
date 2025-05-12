package packup.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.user.domain.UserInfo;
import packup.user.dto.UserInfoResponse;
import packup.user.dto.UserPreferRequest;
import packup.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResultModel<UserInfoResponse> getMyInfo(@Auth Long memberId) {
        UserInfoResponse userInfoResponse = userService.getUserInfo(memberId);
        return ResultModel.success(userInfoResponse);
    }

    @PutMapping("/prefer")
    public ResultModel<Void> updateUserPrefer(
            @Auth Long memberId,
            @RequestBody UserPreferRequest request) {

        userService.updateUserPrefer(memberId, request);
        return ResultModel.success();
    }




}
