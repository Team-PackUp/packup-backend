package packup.recommendation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import packup.common.domain.BaseEntity;

@Entity
@Getter
@Setter
@Table(name = "recommend")
public class Recommend extends BaseEntity {

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "tour_seq", nullable = false)
    private Long tourSeq;

    @Column(name = "score", nullable = false)
    private Float score;
}
