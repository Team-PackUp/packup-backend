package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.chat.presentation.BooleanToCharConverter;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

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
    @JoinColumn(name = "chat_room_seq", nullable = false)
    private ChatRoom chatRoomSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "file_flag", columnDefinition = "CHAR(1)")
    @Convert(converter = BooleanToCharConverter.class)
    private Boolean fileFlag;

    @PrePersist
    private void prePersist() {
        if (fileFlag == null) {
            fileFlag = false; // 기본 값 'false'
        }
    }
}
