package packup.firebase.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.firebase.domain.UserFcmToken;
import packup.user.domain.UserInfo;

import java.util.List;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    List<UserFcmToken> findAllByUserSeqIn(List<UserInfo> userList);
}
