package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
