package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;
import packup.user.domain.UserWithDrawLog;

import java.util.Optional;

public interface UserWithDrawRepository extends JpaRepository<UserWithDrawLog, Long> {
}
