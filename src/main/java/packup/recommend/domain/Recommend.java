package packup.recommend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packup.common.domain.BaseEntity;
import packup.recommend.enums.ActionType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    @Column(name = "score", nullable = false)
    private Float score;
}
