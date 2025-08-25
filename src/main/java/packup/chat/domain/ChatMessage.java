package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_seq", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType fileFlag;

    private ChatMessage(ChatRoom chatRoom, Long userSeq, String message, YnType fileFlag) {
        this.chatRoom = chatRoom;
        this.userSeq = userSeq;
        this.message = message;
        this.fileFlag = fileFlag;
    }

    public static ChatMessage of(ChatRoom chatRoom, Long userSeq, String message, YnType fileFlag) {
        return new ChatMessage(chatRoom, userSeq, message, fileFlag);
    }
}

