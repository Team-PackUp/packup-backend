package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;
import packup.chat.presentation.ChatConverter;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

    @Convert(converter = ChatConverter.class)
    @Column(name = "part_user_seq", columnDefinition = "jsonb", nullable = false)
    private List<Long> partUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @Column(name = "title", nullable = false)
    private String title;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private ChatRoom(List<Long> partUserSeq, UserInfo user, String title) {
        this.partUserSeq = partUserSeq;
        this.user = user;
        this.title = title;
    }

    public static ChatRoom of(List<Long> partUserSeq, UserInfo user, String title) {
        return new ChatRoom(partUserSeq, user, title);
    }

    public void updateChatLastDate() {
        this.updatedAt = LocalDateTime.now();
    }
}
