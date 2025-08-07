package packup.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import packup.chat.domain.ChatMessageFile;
import packup.chat.exception.ChatException;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.dto.FileResponse;
import packup.common.enums.YnType;
import packup.common.util.FileUtil;
import packup.common.util.JsonUtil;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;
import packup.user.domain.UserWithDrawLog;
import packup.user.domain.repository.UserDetailInfoRepository;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.domain.repository.UserPreferRepository;
import packup.user.domain.repository.UserWithDrawRepository;
import packup.user.dto.*;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

import java.io.IOException;
import java.util.List;

import static packup.chat.exception.ChatExceptionType.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final UserPreferRepository userPreferRepository;
    private final UserDetailInfoRepository userDetailInfoRepository;
    private final UserWithDrawRepository userWithDrawRepository;
    private final CommonCodeRepository commonCodeRepository;

    private final FileUtil fileUtil;

    public UserInfoResponse getUserInfo(Long memberId) {
        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        return UserInfoResponse.of(userInfo);
    }

    @Transactional
    public void updateUserPrefer(Long memberId, UserPreferRequest request) {
        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        List<String> categoryList = request.getPreferCategories();
        String userPreferJson = JsonUtil.toJson(categoryList == null ? List.of() : categoryList);

        UserPrefer userPrefer = userPreferRepository.findByUser(userInfo)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER_PREFER));

        userPrefer.updatePreferCategory(userPreferJson);
    }

    @Transactional
    public void updateUserDetail(Long memberId, UserDetailRequest request) {
        UserInfo user = userInfoRepository.findById(memberId)
            .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        String genderCode = commonCodeRepository.findByCodeName(request.getUserGender())
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_GENDER))
                .getCodeId();

        String nationCode = commonCodeRepository.findByCodeName(request.getUserNation())
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_NATION))
                .getCodeId();

        UserDetailInfo detail = userDetailInfoRepository.findByUser(user)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER_DETAIL));

        detail.updateBasicInfo(
                genderCode,
                nationCode,
                Integer.parseInt(request.getUserAge())
        );
    }

    @Transactional
    public void updateSettingPush(Long memberId, SettingPushRequest request) {
        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        UserDetailInfo detail = userDetailInfoRepository.findByUser(user)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER_DETAIL));

        detail.updateSettingPush(request.getPushFlag(), request.getMarketingFlag());
    }

    @Transactional
    public void updateUserProfile(Long memberId, UserProfileRequest request) {
        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        UserDetailInfo detail = userDetailInfoRepository.findByUser(user)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER_DETAIL));

        UserPrefer prefer = userPreferRepository.findByUser(user)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER_PREFER));

        if(!checkDuplicateNickName(user, request.getNickName())) {
            throw  new UserException(UserExceptionType.ALREADY_NICKNAME);
        }

        // 회원 상세정보 수정
        detail.updateUserProfile(request);

        // 회원 선호아이템 수정
        prefer.updatePreferCategory(JsonUtil.toJson(request.getPreference()));
    }

    public FileResponse updateUserProfileImage(Long memberId, String type, MultipartFile file) throws IOException {

        userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        return fileUtil.saveImage(type, file);
    }

    @Transactional
    public void userWithDraw(Long memberId, UserWithDrawLogRequest request) {
        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        if(user.getWithdrawFlag() == YnType.Y) {
            throw new UserException(UserExceptionType.ALREADY_WITHDRAW);
        }

        // 탈퇴 처리
        user.withdrawOrReRegister(YnType.Y);

        // 로그 기록
        saveUserWithDrawLog(user, request);
    }

    // 재가입
    public void userReRegister(UserInfo user) {
        user.withdrawOrReRegister(YnType.N);

        UserWithDrawLogRequest userWithDrawLogRequest = UserWithDrawLogRequest.builder()
                .reason("재가입")
                .codeName("RE-REGISTER")
                .build();

        saveUserWithDrawLog(user, userWithDrawLogRequest);
    }

    public void saveUserWithDrawLog(UserInfo user, UserWithDrawLogRequest request) {
        // 탈퇴 로그
        String logTypeCode = commonCodeRepository.findByCodeName(request.getCodeName())
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_GENDER))
                .getCodeId();

        UserWithDrawLog userWithDrawLog = UserWithDrawLog.of(
                user, request.getReason(), logTypeCode
        );

        userWithDrawRepository.save(userWithDrawLog);
    }

    private boolean checkDuplicateNickName(UserInfo user, String nickName) {

        boolean result = userDetailInfoRepository.existsByUserNotAndNickname(user, nickName);
        return !result;
    }
}
