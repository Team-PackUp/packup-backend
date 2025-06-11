package packup.tour.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import packup.common.dto.ResultModel;
import packup.tour.dto.TourInfoResponse;
import packup.tour.service.TourService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour")
public class TourApiController {

    private final TourService tourService;

    @GetMapping
    public ResultModel<Page<TourInfoResponse>> getTours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TourInfoResponse> tours = tourService.getTours(page, size);
        return ResultModel.success(tours);
    }

}
