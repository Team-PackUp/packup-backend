package packup.guide.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "guide_info")
public class GuideInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false, unique = true)
    private UserInfo user;

    @Column(name= "guide_name", nullable = false)
    private String guideName;

    @Column(name = "tel_number", length = 11, nullable = false, unique = true)
    private String telNumber;

    @Column(name = "tel_number2", length = 11, nullable = true, unique = true)
    private String telNumber2 = "";  // 기본값 공백

    /**
     * PostgreSQL의 jsonb 컬럼 매핑
     * 예: ["ko", "en", "ja"]
     */
    @Type(JsonBinaryType.class)
    @Column(name = "language", columnDefinition = "jsonb", nullable = false)
    private JsonNode languages;

    @Lob
    @Column(name = "guide_introduce")
    private String guideIntroduce;

    @Column(name = "guide_rating", columnDefinition = "smallint", nullable = false)
    private short guideRating = 0;  // 기본값 0

    /**
     * 가이드 아바타 이미지 경로 (파일 시스템 또는 URL)
     */
    @Column(name = "guide_avatar_path", length = 255)
    private String guideAvatarPath;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;
}
