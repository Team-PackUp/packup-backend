package packup.tour.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.dto.PageResponse;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 투어 수정
     */
    @Transactional
    public TourInfoResponse updateTour(TourInfoUpdateRequest request) {
        // 1. 투어 조회
        TourInfo tour = tourInfoRepository.findById(request.getSeq())
                .orElseThrow(() -> new EntityNotFoundException("해당 투어가 존재하지 않습니다."));

        // 2. 업데이트 수행
        tour.update(
                request.getMinPeople(),
                request.getMaxPeople(),
                request.getApplyStartDate(),
                request.getApplyEndDate(),
                request.getTourStartDate(),
                request.getTourEndDate(),
                request.getTourTitle(),
                request.getTourPrice(),
                request.getTourIntroduce(),
                request.getTourStatusCode(),
                request.getTourLocation(),
                request.getTitleImagePath()
        );

        // 3. 응답 DTO로 변환하여 반환
        return TourInfoResponse.from(tour);
    }

    public List<TourInfoResponse> popularTour(int count) {

        List<TourInfo> latestTour = new ArrayList<>(
                tourInfoRepository.findLatest(LocalDate.now(), PageRequest.of(0, 50))
                        .getContent()); //getContent or toList는 읽기 전용이라 복사 데이터 생성 후 shuffle


        if(!latestTour.isEmpty()) {
            Collections.shuffle(latestTour);
        }

        return latestTour.stream()
                .limit(count)
                .map(TourInfoResponse::from)
                .toList();
    }
}
