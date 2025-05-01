package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_seq", nullable = false, unique = true)
    private ChatRoom chatRoomSeq;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
