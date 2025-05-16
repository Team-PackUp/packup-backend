package packup.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.domain.value.ApplyPeriod;
import packup.tour.domain.value.TourPeriod;
import packup.tour.dto.TourCreateRequest;
import packup.tour.dto.TourDetailResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideTourService {

    private final TourInfoRepository tourInfoRepository;

    public Long createTour(String guideSeq, TourCreateRequest request) {
        TourInfo tour = TourInfo.builder()
                .guideSeq(Long.parseLong(guideSeq))
                .minimumPeople(request.getMinimumPeople())
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

    //투어 수정
//    public void updateTour() {
//
//    }

    //투어 조회
//    public List<TourDetailResponse> getTours(Long guideId) {
//        // 조회 후 DTO 변환
//    }
}