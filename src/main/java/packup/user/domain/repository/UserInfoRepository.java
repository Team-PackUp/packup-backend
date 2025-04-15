package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.user.domain.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
