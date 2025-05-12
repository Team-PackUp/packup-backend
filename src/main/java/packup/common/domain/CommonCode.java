package packup.common.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import packup.common.enums.YnType;

import java.time.LocalDateTime;

@Entity
@Table(name = "common_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonCode {

    @Id
    @Column(name = "code_id", length = 50)
    private String codeId;

    @Column(name = "code_name", nullable = false, length = 100)
    private String codeName;

    @Column(name = "parent_code_id", length = 50)
    private String parentCodeId;

    @Column(name = "code_order")
    private Integer codeOrder;

    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_flag", columnDefinition = "yn_enum", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private YnType deleteFlag;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 상태 변경 메서드 추가 (선택사항)
    public void markAsDeleted() {
        this.deleteFlag = YnType.Y;
    }
}
