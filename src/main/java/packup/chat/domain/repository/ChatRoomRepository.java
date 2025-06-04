package packup.chat.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.chat.domain.ChatRoom;

import java.util.Map;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = """
SELECT 
  cr.*, 
  
  -- 읽지 않은 메시지 수
  (
    SELECT COUNT(*) 
    FROM chat_message cm 
    WHERE cm.chat_room_seq = cr.seq
      AND cm.user_seq != :memberId
      AND cm.created_at > COALESCE((
          SELECT updated_at 
          FROM chat_read 
          WHERE user_seq = :memberId AND chat_room_seq = cr.seq
      ), '1970-01-01')
  ) AS unread_count,

  -- 가장 최근 메시지 내용
  (
    SELECT cm.message
    FROM chat_message cm
    WHERE cm.chat_room_seq = cr.seq
    ORDER BY cm.created_at DESC
    LIMIT 1
  ) AS last_message,

  -- 가장 최근 메시지 날짜
  (
    SELECT cm.created_at
    FROM chat_message cm
    WHERE cm.chat_room_seq = cr.seq
    ORDER BY cm.created_at DESC
    LIMIT 1
  ) AS last_message_date

FROM chat_room cr
WHERE cr.part_user_seq @> to_jsonb(array[:memberId]::int[])
ORDER BY cr.updated_at DESC, cr.seq DESC
""",

            countQuery = """
SELECT COUNT(*) FROM chat_room cr 
WHERE cr.part_user_seq @> to_jsonb(array[:memberId]::int[])
""",

            nativeQuery = true)
    Page<Map<String, Object>> findChatRoomListWithUnreadCount(
            @Param("memberId") long memberId,
            Pageable pageable
    );



    ChatRoom findFirstBySeq(long seq);
}
