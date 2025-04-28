package packup.chat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import packup.chat.presentation.ChatRoomConverter;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    private Long seq;

    @Convert(converter = ChatRoomConverter.class)
    @Column(name = "part_user_seq", columnDefinition = "jsonb", nullable = false)
    private List<Long> partUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo userSeq;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
