package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.chat.domain.ChatRead;
import packup.chat.domain.ChatRoom;
import packup.user.domain.UserInfo;

import java.util.Optional;

public interface ChatReadRepository extends JpaRepository<ChatRead, Long> {
    Optional<ChatRead> findChatReadByUserAndChatRoomSeq(UserInfo userInfo, ChatRoom chatRoom);
}
