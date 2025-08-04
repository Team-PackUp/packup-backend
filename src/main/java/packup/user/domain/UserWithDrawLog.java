package packup.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import packup.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name = "user_withdraw_log")
public class UserWithDrawLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @Column(name = "reason")
    private String reason;

    @Column(name = "log_type")
    private String logType;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private UserWithDrawLog(UserInfo user, String reason, String logType) {
        this.user = user;
        this.reason = reason;
        this.logType = logType;
    }

    public static UserWithDrawLog of(UserInfo user, String reason, String logType) {
        return new UserWithDrawLog(user, reason, logType);
    }
}
