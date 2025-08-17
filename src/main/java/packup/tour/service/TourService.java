package packup.tour.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.dto.PageDTO;
import packup.common.dto.PageResponse;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.tourInfo.TourInfoResponse;
import packup.tour.dto.tourInfo.TourInfoUpdateRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static packup.tour.constant.tourConstant.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourInfoRepository tourInfoRepository;

    /**
     * 전체 투어 목록을 페이징하여 조회합니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param size 페이지당 항목 수
     * @return 투어 정보 응답 객체의 페이지(Page)
     */
    public PageResponse<TourInfoResponse> getTours(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("seq").descending());
        Page<TourInfo> tourPage = tourInfoRepository.findAll(pageable);
        Page<TourInfoResponse> dtoPage = tourPage.map(TourInfoResponse::from);

        return new PageResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber() + 1,
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages(),
                dtoPage.isLast(),
                dtoPage.isFirst(),
                dtoPage.isEmpty()
        );
    }

    @Transactional
    public TourInfoResponse updateTour(TourInfoUpdateRequest request) {
        // 1) 대상 투어 조회
        TourInfo tour = tourInfoRepository.findById(request.getSeq())
                .orElseThrow(() -> new EntityNotFoundException("해당 투어가 존재하지 않습니다."));

        // 2) 부분 갱신 (null 이 아닌 값만 반영)
        tour.update(
                coalesce(request.getTourTitle(),            tour.getTourTitle()),
                coalesce(request.getTourIntroduce(),        tour.getTourIntroduce()),
                coalesce(request.getTourIncludedContent(),  tour.getTourIncludedContent()),
                coalesce(request.getTourExcludedContent(),  tour.getTourExcludedContent()),
                coalesce(request.getTourNotes(),            tour.getTourNotes()),
                coalesce(request.getTourLocationCode(),     tour.getTourLocationCode()),
                coalesce(request.getTourThumbnailUrl(),     tour.getTourThumbnailUrl()),
                coalesce(request.getTourPrice(),            tour.getTourPrice()),
                coalesce(request.getMinHeadCount(),         tour.getMinHeadCount()),
                coalesce(request.getMaxHeadCount(),         tour.getMaxHeadCount()),
                coalesce(request.getMeetUpAddress(),        tour.getMeetUpAddress()),
                coalesce(request.getMeetUpLat(),            tour.getMeetUpLat()),
                coalesce(request.getMeetUpLng(),            tour.getMeetUpLng()),
                coalesce(request.getTransportServiceFlag(), tour.getTransportServiceFlag()),
                coalesce(request.getPrivateFlag(),          tour.getPrivateFlag()),
                coalesce(request.getPrivatePrice(),         tour.getPrivatePrice()),
                coalesce(request.getAdultContentFlag(),     tour.getAdultContentFlag()),
                coalesce(request.getTourStatusCode(),       tour.getTourStatusCode()),
                coalesce(request.getApprovalAdminSeq(),     tour.getApprovalAdminSeq()),
                coalesce(request.getRejectReason(),         tour.getRejectReason()),
                coalesce(request.getDeletedFlag(),          tour.getDeletedFlag()),
                coalesce(request.getMemo(),                 tour.getMemo())
        );

        // 3) 응답 DTO 변환
        return TourInfoResponse.from(tour);
    }

    // null 이 아닌 경우에만 새 값 사용
    private static <T> T coalesce(T newVal, T oldVal) {
        return newVal != null ? newVal : oldVal;
    }


    public PageDTO<TourInfoResponse> popularTour(int count, int page) {

        List<TourInfo> shuffled = new ArrayList<>(tourInfoRepository.findLatest(
                LocalDate.now(),
                PageRequest.of(0, 50)).getContent());
        if (!shuffled.isEmpty()) {
            Collections.shuffle(shuffled);
        }

        List<TourInfoResponse> content = shuffled.stream()
                .limit(count)
                .map(TourInfoResponse::from)
                .toList();

        Page<TourInfoResponse> resultPage = new PageImpl<>(
                content,
                PageRequest.of(page, PAGE_SIZE),
                shuffled.size()
        );

        return PageDTO.of(resultPage);
    }

}
