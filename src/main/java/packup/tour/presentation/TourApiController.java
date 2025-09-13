package packup.tour.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.PageDTO;
import packup.common.dto.PageResponse;
import packup.common.dto.ResultModel;
import packup.recommend.dto.RecommendResponse;
import packup.recommend.exception.RecommendException;
import packup.tour.dto.tourBooking.TourBookingCreateRequest;
import packup.tour.dto.tourBooking.TourBookingResponse;
import packup.tour.dto.tourInfo.TourInfoResponse;
import packup.tour.dto.tourInfo.TourInfoUpdateRequest;
import packup.tour.dto.tourListing.TourCreateRequest;
import packup.tour.dto.tourListing.TourListingDetailResponse;
import packup.tour.dto.tourListing.TourListingResponse;
import packup.tour.dto.tourSession.TourSessionCreateRequest;
import packup.tour.dto.tourSession.TourSessionOpenResponse;
import packup.tour.dto.tourSession.TourSessionResponse;
import packup.tour.service.TourBookingService;
import packup.tour.service.TourService;

import java.time.LocalDateTime;
import java.util.List;

import static packup.recommend.exception.RecommendExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour")
public class TourApiController {

    private final TourService tourService;
    private final TourBookingService tourBookingService;

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

    @GetMapping("/proxy/{regionCode}")
    public ResultModel<PageResponse<TourInfoResponse>> getToursByRegion(
            @PathVariable String regionCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<TourInfoResponse> tours = tourService.getTourByRegion(regionCode, page, size);
        return ResultModel.success(tours);
    }

    @GetMapping("/guide/{guideSeq}")
    public ResultModel<List<TourInfoResponse>> getToursByGuide(@PathVariable Long guideSeq) {
        List<TourInfoResponse> tours = tourService.getTourByGuide(guideSeq);
        return ResultModel.success(tours);
    }

    @GetMapping("/detail/{tourSeq}")
    public ResultModel<TourInfoResponse> getTourDetail(@PathVariable Long tourSeq) {
        TourInfoResponse tours = tourService.getTourDetail(tourSeq);
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

    @GetMapping("/recommend")
    public ResultModel<RecommendResponse> recommendForUser(@RequestParam Integer count, @RequestParam Integer page) {

        if(count == null || count < 1) {
            throw new RecommendException(ABNORMAL_ACCESS);
        }

        // 최근껏 중에서 랜덤하게 추출
        PageDTO<TourInfoResponse> popularResponseList = tourService.popularTour(count, page);

        RecommendResponse recommendResponse = RecommendResponse.builder()
                .popular(popularResponseList)
                .build();

        return ResultModel.success(recommendResponse);
    }


    @GetMapping("/me/listings")
    public ResultModel<PageResponse<TourListingResponse>> getMyListings(
            @Auth Long memberSeq,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageResponse<TourListingResponse> body = tourService.getMyListings(memberSeq, page, size);
        return ResultModel.success(body);
    }

    @PostMapping("/listing")
    public ResultModel<TourInfoResponse> createTour(
            @Auth Long memberSeq,
            @Valid @RequestBody TourCreateRequest request
    ) {
        TourInfoResponse created = tourService.createTour(memberSeq, request);
        return ResultModel.success(created);
    }

    @GetMapping("/{tourSeq}/sessions")
    public ResultModel<List<TourSessionResponse>> getSessions(@PathVariable Long tourSeq) {
        return ResultModel.success(tourService.getSessions(tourSeq));
    }

    @PostMapping("/guide/{tourSeq}/sessions")
    public ResultModel<TourSessionResponse> createSession(
            @Auth Long memberSeq,
            @PathVariable Long tourSeq,
            @Valid @RequestBody TourSessionCreateRequest request
    ) {
        TourSessionResponse created = tourService.createSession(memberSeq, tourSeq, request);
        return ResultModel.success(created);
    }

    @GetMapping("/listing/{seq}")
    public ResultModel<TourListingDetailResponse> getListingDetail(@PathVariable Long seq) {
        TourListingDetailResponse body = tourService.getListingDetail(seq);
        return ResultModel.success(body);
    }

    @PutMapping("/listing/{seq}")
    public ResultModel<TourListingDetailResponse> updateListing(
            @Auth Long memberSeq,
            @PathVariable Long seq,
            @Valid @RequestBody TourCreateRequest request
    ) {
        TourListingDetailResponse updated = tourService.updateListing(memberSeq, seq, request);
        return ResultModel.success(updated);
    }

    @GetMapping("/{tourSeq}/sessions/open")
    public ResultModel<List<TourSessionOpenResponse>> getOpenSessions(
            @PathVariable Long tourSeq,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from
    ) {
        List<TourSessionOpenResponse> body = tourService.getOpenSessions(tourSeq, from);
        return ResultModel.success(body);
    }

    @PostMapping("/booking")
    public ResultModel<TourBookingResponse> createBooking(
            @Auth Long memberSeq,
            @Valid @RequestBody TourBookingCreateRequest request
    ) {
        TourBookingResponse body = tourBookingService.createBooking(memberSeq, request);
        return ResultModel.success(body);
    }

}