package packup.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.domain.value.ApplyPeriod;
import packup.tour.domain.value.TourPeriod;
import packup.tour.dto.TourCreateRequest;
import packup.tour.dto.TourDetailResponse;
import packup.tour.dto.TourUpdateRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideTourService {

    private final TourInfoRepository tourInfoRepository;

    public Long createTour(String guideSeq, TourCreateRequest request) {
        TourInfo tour = TourInfo.builder()
                .guideSeq(Long.parseLong(guideSeq))
                .minPeople(request.getMinPeople())
                .maxPeople(request.getMaxPeople())
                .applyPeriod(ApplyPeriod.of(request.getApplyStartDate(), request.getApplyEndDate()))
                .tourPeriod(TourPeriod.of(request.getTourStartDate(), request.getTourEndDate()))
                .tourIntroduce(request.getTourIntroduce())
                .tourStatusCode(request.getTourStatusCode())
                .tourLocation(request.getTourLocation())
                .titleImagePath(request.getTitleImagePath())
                .build();

        // 저장 후 DB에서 자동 생성된 PK(seq) 리턴
        return tourInfoRepository.save(tour).getSeq();
    }

    public List<TourDetailResponse> getToursByGuideId(String guideId) {
        Long guideSeq = Long.parseLong(guideId);
        List<TourInfo> tours = tourInfoRepository.findByGuideSeq(guideSeq);

        return tours.stream()
                .map(TourDetailResponse::from)
                .toList();
    }

    public void updateTour(String guideId, Long tourId, TourUpdateRequest request) {
        Long guideSeq = Long.parseLong(guideId);
        TourInfo tour = tourInfoRepository.findByIdAndGuideSeq(tourId, guideSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 투어가 존재하지 않거나 수정 권한이 없습니다."));

        tour.update(
                request.getMinPeople(),
                request.getMaxPeople(),
                ApplyPeriod.of(request.getApplyStartDate(), request.getApplyEndDate()),
                TourPeriod.of(request.getTourStartDate(), request.getTourEndDate()),
                request.getTourIntroduce(),
                request.getTourStatusCode(),
                request.getTourLocation(),
                request.getTitleImagePath()
        );

        tourInfoRepository.save(tour); // JPA 영속성 컨텍스트로 인해 생략해도 무방할 수 있음
    }

    //투어 수정
//    public void updateTour() {
//
//    }

    //투어 조회
//    public List<TourDetailResponse> getTours(Long guideId) {
//        // 조회 후 DTO 변환
//    }
}