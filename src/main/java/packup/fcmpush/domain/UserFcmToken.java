package packup.fcmpush.domain;

import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "user_fcm_token")
public class UserFcmToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserInfo user;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    @Column(name = "os_type")
    private String osType;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType activeFlag;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private UserFcmToken(UserInfo user, String fcmToken, String osType, YnType activeFlag) {
        this.user = user;
        this.fcmToken = fcmToken;
        this.osType = osType;
        this.activeFlag = activeFlag;
    }

    public static UserFcmToken of(UserInfo user, String fcmToken, String osType, YnType activeFlag) {
        return new UserFcmToken(user, fcmToken, osType, activeFlag);
    }

    public void updateUser(UserInfo user) {
     this.user = user;
    }

    public void updateOsType(String osTypeCode) {
        this.osType = osTypeCode;
    }

    public void deactivate() {
        this.activeFlag = YnType.N;
    }

    public void activate() {
        this.activeFlag = YnType.Y;
    }
}

