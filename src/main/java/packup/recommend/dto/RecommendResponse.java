package packup.recommend.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendResponse {
    private final Long userSeq;
    private Long tourSeq;
    private String actionType;
    private final float score;
}
