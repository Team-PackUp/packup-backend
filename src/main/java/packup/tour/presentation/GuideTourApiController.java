package packup.tour.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import packup.tour.dto.TourCreateRequest;
import packup.tour.dto.TourDetailResponse;
import packup.tour.dto.TourUpdateRequest;
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
    @GetMapping
    public ResponseEntity<List<TourDetailResponse>> getMyTours(@AuthenticationPrincipal(expression = "username") String guideId) {
        List<TourDetailResponse> tours = guideTourService.getToursByGuideId(guideId);
        return ResponseEntity.ok(tours);
    }

    /**
     * 투어 등록
     */
    @PostMapping
    public ResponseEntity<Long> createTour(
            @AuthenticationPrincipal(expression = "username") String guideId,
            @RequestBody TourCreateRequest request
    ) {
        Long createdId = guideTourService.createTour(guideId, request);
        return ResponseEntity.ok(createdId);
    }

    /**
     * 투어 수정
     */
    @PutMapping("/{tourId}")
    public ResponseEntity<Void> updateTour(
            @PathVariable Long tourId,
            @AuthenticationPrincipal(expression = "username") String guideId,
            @RequestBody TourUpdateRequest request
    ) {
        guideTourService.updateTour(guideId, tourId, request);
        return ResponseEntity.ok().build();
    }
}
