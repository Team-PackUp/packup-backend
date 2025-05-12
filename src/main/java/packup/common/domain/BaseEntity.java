package packup.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @CreatedDate
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long seq() {
        return seq;
    }

    public LocalDateTime createdDate() {
        return createdAt;
    }
}
