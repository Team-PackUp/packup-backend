package packup.common.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @CreatedDate
    private LocalDateTime createdDate;

    public Long seq() {
        return seq;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }
}
