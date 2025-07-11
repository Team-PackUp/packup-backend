package packup.alert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.LastModifiedDate;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "alert")
public class Alert extends BaseEntity {

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "alert_type", nullable = false)
    private String alertType;

    @Column(name = "read_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType readFlag;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    private Alert(Long userSeq, String alertType) {
        this.userSeq = userSeq;
        this.alertType = alertType;
        this.readFlag = YnType.N;
    }

    public static Alert of(Long userSeq, String alertType) {
        return new Alert(userSeq, alertType);
    }

    public void markRead() {
        this.readFlag = YnType.Y;

    }
}

