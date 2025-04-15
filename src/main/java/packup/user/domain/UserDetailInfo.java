package packup.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import packup.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_detail_info")
public class UserDetailInfo extends BaseEntity {

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

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void assignUser(UserInfo user) {
        this.user = user;
    }
}