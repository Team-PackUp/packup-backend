package packup.firebase.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_fcm_token")
public class UserFcmToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo userSeq;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType activeFlag;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
