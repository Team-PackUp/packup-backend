package packup.chat.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import packup.chat.domain.ChatMessage;
import packup.chat.domain.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoomSeqOrderByCreatedAtDesc(ChatRoom chatRoom, Pageable page);
}
