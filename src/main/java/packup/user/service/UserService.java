package packup.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.util.JsonUtil;
import packup.fcmpush.exception.FcmPushException;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;
import packup.user.domain.repository.UserDetailInfoRepository;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.domain.repository.UserPreferRepository;
import packup.user.dto.UserDetailRequest;
import packup.user.dto.UserInfoResponse;
import packup.user.dto.UserPreferRequest;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

import java.util.List;

import static packup.fcmpush.exception.FcmPushExceptionType.INVALID_OS_TYPE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final UserPreferRepository userPreferRepository;
    private final UserDetailInfoRepository userDetailInfoRepository;
    private final CommonCodeRepository commonCodeRepository;

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
}
