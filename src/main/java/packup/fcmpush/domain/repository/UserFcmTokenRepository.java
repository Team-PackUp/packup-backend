package packup.fcmpush.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.common.enums.YnType;
import packup.fcmpush.domain.UserFcmToken;
import packup.user.domain.UserInfo;

import java.util.List;
import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    Optional<UserFcmToken> findByFcmToken(String fcmToken);
    List<UserFcmToken> findAllByUserSeq(UserInfo userInfo);
    List<UserFcmToken> findAllByUserSeqInAndActiveFlag(List<UserInfo> userSeq, YnType ynType);
}
