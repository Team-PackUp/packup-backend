package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "chat_read", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"chat_room_seq", "user_seq"})
})
public class ChatRead extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_seq", nullable = false)
    private ChatRoom chatRoomSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_seq", nullable = false)
    private ChatMessage lastReadMessageSeq;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private ChatRead(ChatRoom chatRoom, UserInfo user, ChatMessage lastReadMessage) {
        this.chatRoomSeq = chatRoom;
        this.user = user;
        this.lastReadMessageSeq = lastReadMessage;
    }

    public static ChatRead of(ChatRoom chatRoom, UserInfo user, ChatMessage lastReadMessage) {
        return new ChatRead(chatRoom, user, lastReadMessage);
    }

    public void updateLastReadMessage(ChatMessage newLastReadMessage) {
        this.lastReadMessageSeq = newLastReadMessage;
    }
}
