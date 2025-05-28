package packup.recommend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packup.common.domain.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recommend")
public class Recommend extends BaseEntity {

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "tour_seq", nullable = false)
    private Long tourSeq;

//    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "score", nullable = false)
    private Float score;
}
