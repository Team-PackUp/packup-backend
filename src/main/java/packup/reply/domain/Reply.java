package packup.reply.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.LastModifiedDate;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "reply")
public class Reply extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @Column(name = "target_seq", nullable = false)
    private Long targetSeq;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "delete_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType deleteFlag;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Reply(UserInfo user, Long targetSeq, String targetType, String content) {
        this.user = user;
        this.targetSeq = targetSeq;
        this.targetType = targetType;
        this.content = content;
    }

    public static Reply of(UserInfo user, Long targetSeq, String targetType, String content) {
        return new Reply(user, targetSeq, targetType, content);
    }

    public Reply updateContent(String content) {
        this.content = content;
        return this;
    }


    public void deleteContent() {
        this.deleteFlag = YnType.Y;
    }
}
