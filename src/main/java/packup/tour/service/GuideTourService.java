package packup.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoCreateRequest;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideTourService {

    private final TourInfoRepository tourInfoRepository;

    public Long createTour(String guideId, TourInfoCreateRequest request) {
        TourInfo tour = TourInfo.builder()
                .guideSeq(Long.parseLong(guideId))
                .minPeople(request.getMinPeople())
                .maxPeople(request.getMaxPeople())
                .applyStartDate(request.getApplyStartDate())
                .applyEndDate(request.getApplyEndDate())
                .tourStartDate(request.getTourStartDate())
                .tourEndDate(request.getTourEndDate())
                .tourIntroduce(request.getTourIntroduce())
                .tourStatusCode(request.getTourStatusCode())
                .tourLocation(request.getTourLocation())
                .titleImagePath(request.getTitleImagePath())
                .build();

        // 저장 후 DB에서 자동 생성된 PK(seq) 리턴
        return tourInfoRepository.save(tour).getSeq();
    }

    public List<TourInfoResponse> getToursByGuideId(Long guideSeq) {
        List<TourInfo> tours = tourInfoRepository.findByGuideSeq(guideSeq);

        return tours.stream()
                .map(TourInfoResponse::from)
                .toList();
    }

    public void updateTour(Long guideSeq, Long tourSeq, TourInfoUpdateRequest request) {
        TourInfo tour = tourInfoRepository.findBySeqAndGuideSeq(tourSeq, guideSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 투어가 존재하지 않거나 수정 권한이 없습니다."));

        tour.update(
                request.getMinPeople(),
                request.getMaxPeople(),
                request.getApplyStartDate(),
                request.getApplyEndDate(),
                request.getTourStartDate(),
                request.getTourEndDate(),
                request.getTourIntroduce(),
                request.getTourStatusCode(),
                request.getTourLocation(),
                request.getTitleImagePath()
        );

        tourInfoRepository.save(tour);
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