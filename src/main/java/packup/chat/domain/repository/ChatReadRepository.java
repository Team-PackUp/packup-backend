package packup.chat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.chat.domain.ChatRead;
import packup.chat.dto.UnreadMessageProjection;

import java.util.List;

public interface ChatReadRepository extends JpaRepository<ChatRead, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
      insert into chat_read (
        chat_room_seq, user_seq, last_read_message_seq, created_at, updated_at
      )
      values (:roomId, :userId, :lastSeq, now(), now())
      on conflict (chat_room_seq, user_seq)
      do update
        set last_read_message_seq = greatest(chat_read.last_read_message_seq, excluded.last_read_message_seq),
            updated_at = now()
    """, nativeQuery = true)
    void upsertRead(@Param("roomId") long roomId,
                    @Param("userId") long userId,
                    @Param("lastSeq") long lastSeq);

    @Query(value = """
        select u.user_seq as userSeq, coalesce(count(m.seq), 0) as unread
        from unnest(cast(:userSeqs as bigint[])) as u(user_seq)
        left join chat_read r
          on r.chat_room_seq = :roomId and r.user_seq = u.user_seq
        left join chat_message m
          on m.chat_room_seq = :roomId
         and m.user_seq <> u.user_seq
         and m.seq > coalesce(r.last_read_message_seq, 0)
        group by u.user_seq
    """, nativeQuery = true)
    List<UnreadMessageProjection> countUnreadByUsers(@Param("roomId") long roomId,
                                                   @Param("userSeqs") Long[] userSeqs);

}
