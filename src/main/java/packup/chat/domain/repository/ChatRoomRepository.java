package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.chat.domain.ChatRoom;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT * FROM chat_room WHERE part_user_seq @> to_jsonb(array[:seq]::int[])", nativeQuery = true)
    List<ChatRoom> findByPartUserSeqContains(@Param("seq") long seq);

    ChatRoom findFirstBySeq(long seq);
}
