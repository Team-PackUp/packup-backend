package packup.chat.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.chat.domain.ChatRoom;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT * FROM chat_room WHERE part_user_seq @> to_jsonb(array[:memberId]::int[]) ORDER BY updated_at DESC, seq DESC", nativeQuery = true)
    Page<ChatRoom> findByPartUserSeqContains(@Param("memberId") long seq, Pageable page);

    ChatRoom findFirstBySeq(long seq);
}
