package packup.recommend.dto;


import lombok.Builder;
import lombok.Getter;
import packup.common.dto.PageDTO;
import packup.tour.dto.tourInfo.TourInfoResponse;

@Getter
@Builder
public class RecommendResponse {
    private final PageDTO<TourInfoResponse> tour;
    private final PageDTO<TourInfoResponse> popular;
}
