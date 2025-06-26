package packup.recommend.dto;


import lombok.Builder;
import lombok.Getter;
import packup.tour.dto.TourInfoResponse;

@Getter
@Builder
public class RecommendResponse {
    private final TourInfoResponse tour;
}
