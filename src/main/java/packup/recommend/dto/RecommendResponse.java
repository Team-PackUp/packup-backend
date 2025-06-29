package packup.recommend.dto;


import lombok.Builder;
import lombok.Getter;
import packup.tour.dto.TourInfoResponse;

import java.util.List;

@Getter
@Builder
public class RecommendResponse {
    private final List<TourInfoResponse> tour;
    private final List<TourInfoResponse> popular;
}
