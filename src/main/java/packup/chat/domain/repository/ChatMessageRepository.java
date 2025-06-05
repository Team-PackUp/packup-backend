package packup.chat.domain.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import packup.chat.domain.ChatMessage;
import packup.chat.domain.ChatRoom;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoomSeqOrderByCreatedAtDesc(ChatRoom chatRoom, Pageable page);

    int countByChatRoomSeqAndUserNotAndCreatedAtAfter(ChatRoom chatRoomSeq, UserInfo user, LocalDateTime createdAt, Limit limit);
    Optional<ChatMessage> findTopByChatRoomSeqOrderByCreatedAtDesc(ChatRoom chatRoom);

}
