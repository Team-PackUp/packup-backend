package packup.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import packup.common.dto.PageResponse;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;
import packup.tour.enums.TourStatusCode;

@Service
@RequiredArgsConstructor
public class UserTourService {

    private final TourInfoRepository tourInfoRepository;

    /**
     * 유저 조회용 투어 목록을 페이징하여 조회합니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param size 페이지당 항목 수
     * @return 투어 정보 응답 객체의 페이지(Page)
     */
    public PageResponse<TourInfoResponse> getTours(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("seq").descending());
        Page<TourInfo> tourPage = tourInfoRepository.findFilteredTours(TourStatusCode.RECRUITING, pageable);
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
}
