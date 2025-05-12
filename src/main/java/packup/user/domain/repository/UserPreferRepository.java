package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;

import java.util.Optional;

public interface UserPreferRepository extends JpaRepository<UserPrefer, Long> {
    Optional<UserPrefer> findByUser(UserInfo user);
}
