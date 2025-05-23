package packup.notice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;
import packup.common.util.DeltaConverter;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notice")
public class Notice extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content", columnDefinition = "jsonb")
    private String content;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_seq", nullable = false)
//    private UserInfo admin; // 임시로 회원 테이블 사용

    @Column(name = "admin_seq", nullable = false)
    private Long admin; // 임시로 회원 테이블 사용

    @Column(name = "notice_type", nullable = false)
    private String noticeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fcm_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType fcmFlag;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType deleteFlag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
