package packup.tour.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import packup.common.dto.PageResponse;
import packup.common.dto.ResultModel;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;
import packup.tour.service.TourService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour")
public class TourApiController {

    private final TourService tourService;

    /**
     * 전체 투어 목록을 페이징 방식으로 조회합니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param size 페이지당 조회할 투어 개수
     * @return 투어 정보 응답 객체가 포함된 표준 결과 모델
     */
    @GetMapping
    public ResultModel<PageResponse<TourInfoResponse>> getTours(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<TourInfoResponse> tours = tourService.getTours(page, size);
        return ResultModel.success(tours);
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
