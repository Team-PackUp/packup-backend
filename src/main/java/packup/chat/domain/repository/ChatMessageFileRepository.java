package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.chat.domain.ChatMessageFile;

public interface ChatMessageFileRepository extends JpaRepository<ChatMessageFile, Long> {

}
