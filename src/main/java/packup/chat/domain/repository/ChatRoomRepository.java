package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
