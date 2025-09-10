package packup.reply.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.domain.BaseEntity;
import packup.common.dto.FileResponse;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reply_file")
public class ReplyFile extends BaseEntity {

    @Column(name = "reply_file_path", nullable = false)
    private String replyFilePath;

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "encoded_name", nullable = false)
    private String encodedName;

    @Column(name = "real_name", nullable = false)
    private String realName;

    private ReplyFile(String chatFilePath, Long userSeq, String encodedName, String realName) {

        this.replyFilePath = chatFilePath;
        this.userSeq = userSeq;
        this.encodedName = encodedName;
        this.realName = realName;
    }

    public static ReplyFile of(String chatFilePath, Long userSeq, String encodedName, String realName) {
        return new ReplyFile(chatFilePath,  userSeq, encodedName, realName);
    }
}

