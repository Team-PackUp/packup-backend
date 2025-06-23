package packup.tour.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.tour.domain.TourInfo;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;
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

    /**
     * 투어 신규 등록
     */
    @PostMapping
    public ResultModel<TourInfoResponse> createTour(
            @Auth Long memberId,
            @RequestBody TourInfoUpdateRequest request) {
        TourInfoResponse newTour = tourService.createTour(memberId, request);
        return ResultModel.success(newTour);
    }

    /**
     * 투어 정보 수정
     */
    @PutMapping("/{seq}")
    public ResultModel<TourInfoResponse> updateTour(
            @PathVariable Long seq,
            @RequestBody TourInfoUpdateRequest request) {
        request.setSeq(seq); // pathVariable을 DTO에 반영
        TourInfoResponse updatedTour = tourService.updateTour(request);
        return ResultModel.success(updatedTour);
    }

}
