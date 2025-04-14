package packup.user.domain;

import jakarta.persistence.*;
import packup.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_prefer")
public class UserPrefer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    private UserInfo user;

    @Column(name = "prefer_category_seq", columnDefinition = "json")
    private String preferCategorySeqJson;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
