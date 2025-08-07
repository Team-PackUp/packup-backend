package packup.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import packup.auth.annotation.Auth;
import packup.chat.exception.ChatException;
import packup.common.dto.FileResponse;
import packup.common.dto.ResultModel;
import packup.user.dto.*;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;
import packup.user.service.UserService;

import java.io.IOException;

import static packup.chat.exception.ChatExceptionType.ABNORMAL_ACCESS;

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

    @PutMapping("/detail")
    public ResultModel<Void> updateUserDetail(
            @Auth Long memberId,
            @RequestBody UserDetailRequest request) {

        userService.updateUserDetail(memberId, request);
        return ResultModel.success();
    }

    @PostMapping("/update/profile-image")
    public ResultModel<FileResponse> saveUserProfileImage(@Auth Long memberId, @RequestParam("file") MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new UserException(UserExceptionType.ABNORMAL_ACCESS);
        }
        return ResultModel.success(userService.updateUserProfileImage(memberId, "profile", file));
    }

    @PutMapping("/update/profile")
    public ResultModel<Void> updateUserProfile(@Auth Long memberId, @RequestBody UserProfileRequest request) {

        if(request.getLanguage() == null ||
                request.getPreference() == null ||
                request.getNickName() == null
        ) {
            throw new UserException(UserExceptionType.ABNORMAL_ACCESS);
        }

        userService.updateUserProfile(memberId, request);
        return ResultModel.success();
    }


    @PutMapping("/setting-push")
    public ResultModel<Void> updateSettingPush(@Auth Long memberId, @RequestBody SettingPushRequest request) {

        if(request.getPushFlag() == null || request.getMarketingFlag() == null) {
            throw new UserException(UserExceptionType.ABNORMAL_ACCESS);
        }

        userService.updateSettingPush(memberId, request);
        return ResultModel.success();
    }

    @PostMapping("/withdraw")
    public ResultModel<Void> userWithDraw(@Auth Long memberId, @RequestBody UserWithDrawLogRequest request) {

        if(request.getReason() == null || request.getCodeName() == null) {
            throw new UserException(UserExceptionType.ABNORMAL_ACCESS);
        }

        userService.userWithDraw(memberId, request);
        return ResultModel.success();
    }
}
