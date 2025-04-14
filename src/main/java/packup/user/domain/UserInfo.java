package packup.user.domain;

import jakarta.persistence.*;
import packup.common.enums.YnType;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "join_type", nullable = false, length = 1)
    private String joinType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ban_flag", columnDefinition = "yn_enum", nullable = false)
    private YnType banFlag = YnType.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "adult_flag", columnDefinition = "yn_enum", nullable = false)
    private YnType adultFlag = YnType.Y;

    @Enumerated(EnumType.STRING)
    @Column(name = "withdraw_flag", columnDefinition = "yn_enum", nullable = false)
    private YnType withdrawFlag = YnType.N;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserDetailInfo detailInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserPrefer prefer;
}
