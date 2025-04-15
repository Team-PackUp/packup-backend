package packup.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo extends BaseEntity {

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "join_type", nullable = false, length = 1)
    private String joinType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ban_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType banFlag = YnType.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "adult_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType adultFlag = YnType.Y;

    @Enumerated(EnumType.STRING)
    @Column(name = "withdraw_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType withdrawFlag = YnType.N;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserDetailInfo detailInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserPrefer prefer;

    public void addDetailInfo(UserDetailInfo detailInfo) {
        this.detailInfo = detailInfo;
        detailInfo.assignUser(this);
    }
}
