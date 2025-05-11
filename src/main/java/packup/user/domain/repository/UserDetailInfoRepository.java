package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.user.domain.UserDetailInfo;

public interface UserDetailInfoRepository extends JpaRepository<UserDetailInfo, Long> {
}
