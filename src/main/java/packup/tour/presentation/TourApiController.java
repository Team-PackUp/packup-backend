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

    /**
     * 전체 투어 목록을 페이징 방식으로 조회합니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param size 페이지당 조회할 투어 개수
     * @return 투어 정보 응답 객체가 포함된 표준 결과 모델
     */
    @GetMapping
    public ResultModel<Page<TourInfoResponse>> getTours(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TourInfoResponse> tours = tourService.getTours(page, size);
        return ResultModel.success(tours);
    }

}
