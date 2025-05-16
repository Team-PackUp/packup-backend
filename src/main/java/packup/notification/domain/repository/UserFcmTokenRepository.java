package packup.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.notification.domain.UserFcmToken;
import packup.user.domain.UserInfo;

import java.util.List;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    List<UserFcmToken> findAllByUserSeqIn(List<UserInfo> userList);
}
