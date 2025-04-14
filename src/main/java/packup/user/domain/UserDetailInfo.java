package packup.user.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_detail_info")
public class UserDetailInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    private UserInfo user;

    @Column(name = "gender", length = 1)
    private String gender;

    private String nation;

    private Integer age;

    @Column(unique = true)
    private String nickname;

    @Column(name = "fcm_token", unique = true)
    private String fcmToken;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}