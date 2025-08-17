package packup.recommend.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendRequest {
    private final Long  userSeq;
    private final float score;
    private final String actionType;
    private final Long tourSeq;
}
