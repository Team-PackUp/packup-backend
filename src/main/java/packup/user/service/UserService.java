package packup.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.domain.repository.UserPreferRepository;
import packup.user.dto.UserInfoResponse;
import packup.user.dto.UserPreferRequest;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final UserPreferRepository userPreferRepository;

    public UserInfoResponse getUserInfo(Long memberId) {
        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        return UserInfoResponse.of(userInfo);
    }

    @Transactional
    public void updateUserPrefer(Long memberId, UserPreferRequest request) {
        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        UserPrefer userPrefer = userPreferRepository.findByUser(userInfo)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER_PREFER));

        userPrefer.updatePreferCategory(request.getPreferCategorySeqJson());
    }
}
