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
import java.time.LocalDate;

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

    private LocalDate birth;

    @Column(unique = true)
    private String nickname;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "language", nullable = false)
    private String language;

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

    public void updateBasicInfo(String gender, String nation, LocalDate birth, String language) {
        this.gender = gender;
        this.nation = nation;
        this.birth = birth;
        this.language = language;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSettingPush(YnType pushFlag, YnType marketingFlag) {
        if(this.pushFlag != pushFlag) this.pushFlag = pushFlag;
        if(this.marketingFlag != marketingFlag) this.marketingFlag = marketingFlag;
    }

    public void updateProfile(String profileImagePath, String nickname, LocalDate birth, String gender) {
        if (!Objects.equals(this.profileImagePath, profileImagePath)) {
            this.profileImagePath = profileImagePath;
        }
        if (!Objects.equals(this.nickname, nickname)) {
            this.nickname = nickname;
        }
        if (birth != null && !Objects.equals(this.birth, birth)) {
            this.birth = birth;
        }
        if (!Objects.equals(this.gender, gender)) {
            this.gender = gender;
        }
    }


}