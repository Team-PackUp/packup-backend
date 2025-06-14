package packup.tour.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.tour.dto.TourInfoCreateRequest;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;
import packup.tour.service.GuideTourService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour/guide")
public class GuideTourApiController {

    private final GuideTourService guideTourService;

    /**
     * 등록한 투어 목록 조회
     */
    @GetMapping("/my-tour")
    public ResultModel<List<TourInfoResponse>> getMyTours(@Auth Long memberId) {
        List<TourInfoResponse> tours = guideTourService.getToursByGuideId(memberId);
        return ResultModel.success(tours);
    }

    /**
     * 투어 등록
     */
    @PostMapping("/my-tour")
    public ResultModel<Long> createTour(
            @AuthenticationPrincipal(expression = "username") String guideId,
            @RequestBody TourInfoCreateRequest request
    ) {
        Long createdId = guideTourService.createTour(guideId, request);
        return ResultModel.success(createdId);
    }

    /**
     * 투어 수정
     */
    @PutMapping("/my-tour")
    public ResultModel<Void> updateTour(
            @PathVariable Long tourId,
            @Auth Long memberId,
            @RequestBody TourInfoUpdateRequest request
    ) {
        guideTourService.updateTour(memberId, tourId, request);
        return ResultModel.success();
    }

    /**
     * 투어 삭제
     */
    @DeleteMapping("/my-tour")
    public ResultModel<Void> deleteTour(
            @PathVariable Long tourId,
            @Auth Long memberId,
            @RequestBody TourInfoUpdateRequest request
    ) {
        guideTourService.updateTour(memberId, tourId, request);
        return ResultModel.success();
    }
}
