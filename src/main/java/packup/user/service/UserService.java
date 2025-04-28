package packup.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.dto.UserInfoResponse;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoResponse getUserInfo(Long memberId) {
        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        return UserInfoResponse.of(userInfo);
    }
}
