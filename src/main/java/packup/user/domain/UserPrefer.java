package packup.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import packup.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name = "user_prefer")
public class UserPrefer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    private UserInfo user;

    @Column(name = "prefer_category_seq", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String preferCategorySeqJson;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void assignUser(UserInfo userInfo) {
        this.user = userInfo;
    }

    // DDD 에서는 엔티티에 비즈니스 로직 추가
    public void updatePreferCategory(String preferCategorySeqJson) {
        this.preferCategorySeqJson = preferCategorySeqJson;
        this.updatedAt = LocalDateTime.now();
    }


}
