package packup.tour.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourInfoRepository tourInfoRepository;

    public Page<TourInfoResponse> getTours(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("seq").descending());

        Page<TourInfo> tourPage = tourInfoRepository.findAll(pageable);
        return tourPage.map(TourInfoResponse::from);
    }

    /**
     * 투어 등록
     */
    @Transactional
    public TourInfoResponse createTour(Long memberId, TourInfoUpdateRequest request) {
        TourInfo newTour = TourInfo.builder()
                .guideSeq(memberId) // 예시: 인증된 가이드 번호 (실제로는 SecurityContext 등에서 얻어야 함)
                .minPeople(request.getMinPeople())
                .maxPeople(request.getMaxPeople())
                .applyStartDate(request.getApplyStartDate())
                .applyEndDate(request.getApplyEndDate())
                .tourStartDate(request.getTourStartDate())
                .tourEndDate(request.getTourEndDate())
                .tourIntroduce(request.getTourIntroduce())
                .tourTitle(request.getTourTitle())
                .tourStatusCode(request.getTourStatusCode())
                .tourLocation(request.getTourLocation())
                .titleImagePath(request.getTitleImagePath())
                .build();

        TourInfo savedTour =  tourInfoRepository.save(newTour);

        return TourInfoResponse.from(savedTour);
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
                request.getTourIntroduce(),
                request.getTourStatusCode(),
                request.getTourLocation(),
                request.getTitleImagePath()
        );

        // 3. 응답 DTO로 변환하여 반환
        return TourInfoResponse.from(tour);
    }
}
