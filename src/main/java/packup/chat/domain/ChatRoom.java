package packup.chat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.chat.presentation.ChatRoomConverter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    private Long seq;

    @Convert(converter = ChatRoomConverter.class)
    @Column(name = "part_user_seq", columnDefinition = "TEXT", nullable = false)
    private List<Integer> partUserSeq;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;
}
