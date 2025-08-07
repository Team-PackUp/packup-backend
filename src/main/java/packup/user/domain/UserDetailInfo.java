package packup.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;
import packup.user.dto.UserProfileRequest;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_detail_info")
@DynamicInsert
@DynamicUpdate
public class UserDetailInfo extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    private UserInfo user;

    private String gender;

    private String nation;

    private Integer age;

    @Column(unique = true)
    private String nickname;

    @Column(name = "fcm_token", unique = true)
    private String fcmToken;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "marketing_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType marketingFlag = YnType.N;

    @Column(name = "push_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType pushFlag = YnType.N;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void assignUser(UserInfo userInfo) {
        this.user = userInfo;
    }

    public void updateBasicInfo(String gender, String nation, int age) {
        this.gender = gender;
        this.nation = nation;
        this.age = age;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSettingPush(YnType pushFlag, YnType marketingFlag) {
        if(this.pushFlag != pushFlag) this.pushFlag = pushFlag;
        if(this.marketingFlag != marketingFlag) this.marketingFlag = marketingFlag;
    }

    public void updateUserProfile(UserProfileRequest request) {
        if (!Objects.equals(this.profileImagePath, request.getProfileImagePath())) {
            this.profileImagePath = request.getProfileImagePath();
        }

        if (!Objects.equals(this.nickname, request.getNickName())) {
            this.nickname = request.getNickName();
        }

        if (request.getAge() != null) {
            int requestAge = Integer.parseInt(request.getAge());
            if (requestAge != this.age) {
                this.age = requestAge;
            }
        }

        if (!Objects.equals(this.gender, request.getGender())) {
            this.gender = request.getGender();
        }
    }

}