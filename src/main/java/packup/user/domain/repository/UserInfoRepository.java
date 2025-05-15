package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.user.domain.UserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
    List<UserInfo> findAllBySeqIn(Collection<Long> seq);
}
