package packup.recommend.dto;


import lombok.*;
import packup.recommend.enums.ActionType;

@Getter
@Builder
public class RecommendResponse {
    private final Long userSeq;
    private Long tourSeq;
    private ActionType actionType;
    private final float score;
}
