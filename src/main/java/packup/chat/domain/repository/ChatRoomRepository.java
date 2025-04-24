package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.user.domain.UserInfo;

public interface ChatRoomRepository extends JpaRepository<UserInfo, Long> {

}
