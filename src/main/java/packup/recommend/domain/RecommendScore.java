package packup.recommend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import packup.common.domain.BaseEntity;
import packup.recommend.enums.ActionType;

import javax.swing.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "recommend_score")
public class RecommendScore extends BaseEntity {

//    @Column(name = "user_seq", nullable = false)
//    private Long userSeq;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "score", nullable = false)
    private Float score;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
