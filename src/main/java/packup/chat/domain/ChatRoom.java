package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import packup.chat.presentation.ChatConverter;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

    @Convert(converter = ChatConverter.class)
    @Column(name = "part_user_seq", columnDefinition = "jsonb", nullable = false)
    private List<Long> partUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo userSeq;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
