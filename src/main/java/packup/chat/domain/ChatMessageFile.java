package packup.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message_file")
public class ChatMessageFile extends BaseEntity {

    @Column(name = "chat_file_path", nullable = false)
    private String chatFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo userSeq;

    @Column(name = "encoded_name", nullable = false)
    private String encodedName;

    @Column(name = "real_name", nullable = false)
    private String realName;
}
