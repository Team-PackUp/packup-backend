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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @Column(name = "encoded_name", nullable = false)
    private String encodedName;

    @Column(name = "real_name", nullable = false)
    private String realName;

    private ChatMessageFile(String chatFilePath, UserInfo user, String encodedName, String realName) {
        this.chatFilePath = chatFilePath;
        this.user = user;
        this.encodedName = encodedName;
        this.realName = realName;
    }

    public static ChatMessageFile of(String chatFilePath, UserInfo user, String encodedName, String realName) {
        return new ChatMessageFile(chatFilePath, user, encodedName, realName);
    }
}

