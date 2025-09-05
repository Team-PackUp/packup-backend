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
import packup.tour.domain.repository.TourInfoRepository;
import packup.tour.dto.tourInfo.TourInfoCreateRequest;
import packup.tour.dto.tourInfo.TourInfoResponse;
import packup.tour.dto.tourInfo.TourInfoUpdateRequest;
import packup.tour.exception.GuideTourAccessDeniedException;

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

    /**
     * 투어 등록
     * @param memberId
     * @param request
     * @return
     */
    @Transactional
    public TourInfoResponse createTour(Long memberId, TourInfoCreateRequest request) {
        // 1) 가이드 조회
        GuideInfo guide = guideInfoRepository.findByUser_Seq(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가이드 유저를 찾을 수 없습니다."));

        // 2) 엔티티 생성
        TourInfo newTour = TourInfo.builder()
                .guide(guide)
                .tourTitle(request.getTourTitle())
                .tourIntroduce(request.getTourIntroduce())
                .tourIncludedContent(request.getTourIncludedContent())
                .tourExcludedContent(request.getTourExcludedContent())
                .tourNotes(request.getTourNotes())
                .tourLocationCode(request.getTourLocationCode())
                .tourThumbnailUrl(request.getTourThumbnailUrl())
                .tourPrice(request.getTourPrice())
                .minHeadCount(request.getMinHeadCount())
                .maxHeadCount(request.getMaxHeadCount())
                .meetUpAddress(request.getMeetUpAddress())
                .meetUpLat(request.getMeetUpLat())
                .meetUpLng(request.getMeetUpLng())
                .transportServiceFlag(request.getTransportServiceFlag())
                .privateFlag(request.getPrivateFlag())
                .privatePrice(request.getPrivatePrice())
                .adultContentFlag(request.getAdultContentFlag())
                .tourStatusCode(request.getTourStatusCode())
                .build();

        // 3) 저장
        TourInfo saved = tourInfoRepository.save(newTour);

        // 4) 응답 변환
        return TourInfoResponse.from(saved);
    }

    /**
     * 투어 수정
     * @param memberId
     * @param request
     * @return
     */
    @Transactional
    public TourInfoResponse updateTour(Long memberId, TourInfoUpdateRequest request) {
        // 1) 가이드 조회
        GuideInfo guide = guideInfoRepository.findByUser_Seq(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가이드 유저를 찾을 수 없습니다."));

        // 2) 대상 투어 조회
        TourInfo tour = tourInfoRepository.findById(request.getSeq())
                .orElseThrow(() -> new EntityNotFoundException("해당 투어가 존재하지 않습니다."));

        // 3) 소유권(본인 작성 투어) 검증
        if (!tour.getGuide().getSeq().equals(guide.getSeq())) {
            throw new GuideTourAccessDeniedException(tour.getSeq());
        }

        // 4) 부분 갱신: 요청에 들어온 값만 반영
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

        // 5) 응답 변환
        return TourInfoResponse.from(tour);
    }

    /**
     * null 이 아닐 때만 새 값 사용
     * @param newVal
     * @param oldVal
     * @return
     * @param <T>
     */
    private static <T> T coalesce(T newVal, T oldVal) {
        return newVal != null ? newVal : oldVal;
    }



    //투어 조회
//    public List<TourDetailResponse> getTours(Long guideId) {
//        // 조회 후 DTO 변환
//    }
}