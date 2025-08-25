package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message_file")
public class ChatMessageFile extends BaseEntity {

    @Column(name = "chat_file_path", nullable = false)
    private String chatFilePath;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "encoded_name", nullable = false)
    private String encodedName;

    @Column(name = "real_name", nullable = false)
    private String realName;

    private ChatMessageFile(String chatFilePath, Long userSeq, String encodedName, String realName) {
        this.chatFilePath = chatFilePath;
        this.userSeq = userSeq;
        this.encodedName = encodedName;
        this.realName = realName;
    }

    public static ChatMessageFile of(String chatFilePath, Long userSeq, String encodedName, String realName) {
        return new ChatMessageFile(chatFilePath, userSeq, encodedName, realName);
    }
}

