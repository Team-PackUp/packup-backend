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
import packup.guide.domain.GuideInfo;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;
import packup.tour.dto.TourInfoUpdateRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideTourService {

    private final TourInfoRepository tourInfoRepository;
    private final GuideInfoRepository guideInfoRepository;


    /**
     * 전체 투어 목록을 페이징하여 조회합니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param size 페이지당 항목 수
     * @return 투어 정보 응답 객체의 페이지(Page)
     */
    public PageResponse<TourInfoResponse> getTours(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("seq").descending());
        Page<TourInfo> tourPage = tourInfoRepository.findByGuide_User_Seq(memberId, pageable);
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


    public List<TourInfoResponse> getTours2(Long guideSeq) {
        List<TourInfo> tours = tourInfoRepository.findByGuideSeq(guideSeq);

        return tours.stream()
                .map(TourInfoResponse::from)
                .toList();
    }

    /**
     * 투어 등록
     */
    @Transactional
    public TourInfoResponse createTour(Long memberId, TourInfoUpdateRequest request) {
        GuideInfo guide = guideInfoRepository.findByUser_Seq(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가이드 유저를 찾을 수 없습니다."));

        TourInfo newTour = TourInfo.builder()
                .guide(guide)
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
    public TourInfoResponse updateTour(Long memberId,TourInfoUpdateRequest request) {
        GuideInfo guide = guideInfoRepository.findByUser_Seq(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가이드 유저를 찾을 수 없습니다."));

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


    //투어 조회
//    public List<TourDetailResponse> getTours(Long guideId) {
//        // 조회 후 DTO 변환
//    }
}