package packup.chat.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import packup.chat.domain.ChatMessage;
import packup.chat.dto.ChatMessageResponse;
import packup.chat.dto.LastMessageProjection;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Optional<LastMessageProjection> findTop1ByChatRoomSeqOrderByCreatedAtDesc(Long chatRoom_seq);

    @Query("""
select new packup.chat.dto.ChatMessageResponse(
    m.seq,
    m.chatRoom.seq,
    m.userSeq,
    m.message,
    u.profileImagePath,
    m.createdAt,
    m.fileFlag
)
from ChatMessage m
left join UserDetailInfo u on u.user.seq = m.userSeq
where m.chatRoom.seq = :chatRoomSeq
order by m.createdAt desc
""")
    Page<ChatMessageResponse> findMessages(Long chatRoomSeq, Pageable pageable);
}
