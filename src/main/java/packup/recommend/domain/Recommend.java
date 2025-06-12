package packup.recommend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import packup.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recommend")
public class Recommend extends BaseEntity {

    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "tour_seq", nullable = false)
    private Long tourSeq;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "score", nullable = false)
    private Float score;

    private Recommend(Long userSeq, Long tourSeq, String actionType, Float score) {
        this.userSeq = userSeq;
        this.tourSeq = tourSeq;
        this.actionType = actionType;
        this.score = score;
    }

    public static Recommend of(Long userSeq, Long tourSeq, String actionType, Float score) {
        return new Recommend(userSeq, tourSeq, actionType, score);
    }
}

