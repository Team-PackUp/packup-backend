package packup.tour.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.dto.PageDTO;
import packup.common.dto.PageResponse;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.guide.exception.GuideException;
import packup.guide.exception.GuideExceptionType;
import packup.tour.domain.TourActivity;
import packup.tour.domain.TourActivityThumbnail;
import packup.tour.domain.TourInfo;
import packup.tour.domain.TourSession;
import packup.tour.domain.repository.TourActivityRepository;
import packup.tour.domain.repository.TourActivityThumbnailRepository;
import packup.tour.domain.repository.TourInfoRepository;
import packup.tour.domain.repository.TourSessionRepository;
import packup.tour.dto.tourInfo.TourInfoResponse;
import packup.tour.dto.tourInfo.TourInfoUpdateRequest;
import packup.tour.dto.tourListing.TourCreateRequest;
import packup.tour.dto.tourListing.TourListingDetailResponse;
import packup.tour.dto.tourListing.TourListingResponse;
import packup.tour.dto.tourSession.TourSessionCreateRequest;
import packup.tour.dto.tourSession.TourSessionResponse;
import packup.tour.enums.KrSidoCode;
import packup.tour.enums.TourSessionStatusCode;
import packup.tour.enums.TourStatusCode;
import packup.tour.exception.TourInfoException;
import packup.tour.exception.TourInfoExceptionType;
import packup.tour.exception.TourSessionException;
import packup.tour.exception.TourSessionExceptionType;
import packup.tour.presentation.RegionResolver;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static packup.tour.constant.tourConstant.PAGE_SIZE;
import static packup.tour.exception.TourSessionExceptionType.*;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourInfoRepository tourInfoRepository;
    private final TourSessionRepository tourSessionRepository;
    private final GuideInfoRepository guideInfoRepository;
    private final TourActivityRepository tourActivityRepository;
    private final TourActivityThumbnailRepository tourActivityThumbnailRepository;

    private final RegionResolver regionResolver;

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

    public PageResponse<TourInfoResponse> getTourByRegion(String regionCode, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("seq").descending());
        Page<TourInfo> tourPage = tourInfoRepository.findFilteredToursByRegion(regionCode, pageable);
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

    public List<TourInfoResponse> getTourByGuide(Long guideSeq) {
        GuideInfo guide = guideInfoRepository.findById(guideSeq)
                .orElseThrow(() -> new GuideException(GuideExceptionType.NOT_FOUND_GUIDE));

        List<TourInfo> tours = tourInfoRepository.findAllByGuide(guide);
        return tours.stream()
                .map(TourInfoResponse::from)
                .toList();
    }

    public TourInfoResponse getTourDetail(Long tourSeq) {
        TourInfo tour = tourInfoRepository.findById(tourSeq)
                .orElseThrow(() -> new TourInfoException(TourInfoExceptionType.NOT_FOUND_TOUR));

        return TourInfoResponse.from(tour);
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

    /// ///

    public PageResponse<TourListingResponse> getMyListings(Long memberSeq, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);

        Pageable pageable = PageRequest.of(
                safePage - 1,
                safeSize,
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<TourInfo> p = tourInfoRepository
                .findByGuide_User_SeqAndDeletedFlag(memberSeq, YnType.N, pageable);

        List<TourListingResponse> content = p.getContent()
                .stream()
                .map(TourListingResponse::from)
                .toList();

        return new PageResponse<>(
                content,
                p.getNumber() + 1,
                p.getNumber(),
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages(),
                p.isLast(),
                p.isFirst(),
                p.isEmpty()
        );
    }

    @Transactional
    public TourInfoResponse createTour(Long guideMemberSeq, TourCreateRequest req) {
        GuideInfo guide = guideInfoRepository.findByUser_Seq(guideMemberSeq)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        Long finalLocationCode = null;

        if (req.getTourLocationCode() != null) {
            finalLocationCode = req.getTourLocationCode().longValue();
        } else {
            finalLocationCode = KrSidoCode.fromText(req.getMeetUpAddress())
                    .map(KrSidoCode::getCode)
                    .orElse(null);

            if (finalLocationCode == null &&
                    req.getMeetUpLat() != null && req.getMeetUpLng() != null) {
                finalLocationCode = regionResolver.resolveSidoName(req.getMeetUpLat(), req.getMeetUpLng())
                        .flatMap(KrSidoCode::fromName)
                        .map(KrSidoCode::getCode)
                        .orElse(null);
            }
        }

        TourInfo tour = TourInfo.builder()
                .guide(guide)
                .tourKeywords(req.getTourKeywords() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(req.getTourKeywords()))
                .tourTitle(req.getTourTitle())
                .tourIntroduce(req.getTourIntroduce())
                .tourIncludedContent(req.getTourIncludedContent())
                .tourExcludedContent(req.getTourExcludedContent())
                .tourNotes(req.getTourNotes())
                .tourLocationCode(finalLocationCode)
                .tourThumbnailUrl(req.getTourThumbnailUrl())
                .tourPrice(req.getTourPrice())
                .minHeadCount(req.getMinHeadCount())
                .maxHeadCount(req.getMaxHeadCount())
                .meetUpAddress(req.getMeetUpAddress())
                .meetUpLat(req.getMeetUpLat() == null ? null :
                        BigDecimal.valueOf(req.getMeetUpLat()).setScale(6, RoundingMode.HALF_UP))
                .meetUpLng(req.getMeetUpLng() == null ? null :
                        BigDecimal.valueOf(req.getMeetUpLng()).setScale(6, RoundingMode.HALF_UP))
                .transportServiceFlag("Y".equalsIgnoreCase(req.getTransportServiceFlag()) ? YnType.Y : YnType.N)
                .privateFlag("Y".equalsIgnoreCase(req.getPrivateFlag()) ? YnType.Y : YnType.N)
                .privatePrice(req.getPrivatePrice())
                .adultContentFlag("Y".equalsIgnoreCase(req.getAdultContentFlag()) ? YnType.Y : YnType.N)
                .tourStatusCode(req.getTourStatusCode() == null
                        ? TourStatusCode.RECRUITING
                        : TourStatusCode.fromCode(req.getTourStatusCode()))
                .deletedFlag(YnType.N)
                .memo(req.getMemo())
                .build();

        tour = tourInfoRepository.save(tour);

        if (req.getActivities() != null) {
            for (var a : req.getActivities()) {
                TourActivity act = TourActivity.builder()
                        .tour(tour)
                        .activityOrder(a.getActivityOrder())
                        .activityTitle(a.getActivityTitle())
                        .activityIntroduce(a.getActivityIntroduce())
                        .activityDurationMinute(a.getActivityDurationMinute())
                        .deletedFlag(YnType.N)
                        .build();
                act = tourActivityRepository.save(act);

                if (a.getThumbnails() != null) {
                    for (var th : a.getThumbnails()) {
                        tourActivityThumbnailRepository.save(
                                TourActivityThumbnail.builder()
                                        .tourActivity(act)
                                        .thumbnailOrder(th.getThumbnailOrder())
                                        .thumbnailImageUrl(th.getThumbnailImageUrl())
                                        .build()
                        );
                    }
                }
            }
        }

        return TourInfoResponse.from(tour);
    }


    public List<TourSessionResponse> getSessions(Long tourSeq) {
        return tourSessionRepository.findAllByTour_SeqAndDeletedFlagOrderBySessionStartTimeAsc(tourSeq, YnType.N)
                .stream().map(TourSessionResponse::from).toList();
    }

    @Transactional
    public TourSessionResponse createSession(Long memberSeq, Long pathTourSeq, TourSessionCreateRequest req) {

        // 검증
        if (!req.isValidTimeRange()) {
            throw new TourSessionException(INVALID_TIME_RANGE);
        }
        if (req.getTourSeq() != null && !req.getTourSeq().equals(pathTourSeq)) {
            throw new TourSessionException(MISMATCH_TOUR_SEQ);
        }

        // 투어 존재 확인
        TourInfo tour = tourInfoRepository.findById(pathTourSeq)
                .orElseThrow(() -> new TourSessionException(NOT_FOUND_TOUR));

        // 권한 체크
        // if (!tour.isOwnedBy(memberSeq)) throw new TourSessionException(FORBIDDEN_TOUR_ACCESS);

        LocalDateTime start = req.getSessionStartTime();
        LocalDateTime end   = req.getSessionEndTime();
        boolean overlapped = !tourSessionRepository.findOverlapping(pathTourSeq, start, end, YnType.N).isEmpty();
        if (overlapped) {
            throw new TourSessionException(SESSION_OVERLAPPED);
        }

        Integer statusCode = (req.getSessionStatusCode() != null)
                ? req.getSessionStatusCode()
                : TourSessionStatusCode.OPEN.getCode();

        try {
            TourSession saved = tourSessionRepository.save(
                    TourSession.builder()
                            .tour(tour)
                            .sessionStartTime(start)
                            .sessionEndTime(end)
                            .sessionStatusCode(statusCode)
                            .build()
            );
            return TourSessionResponse.from(saved);
        } catch (Exception e) {
            throw new TourSessionException(FAIL_TO_SAVE_SESSION);
        }
    }


    @Transactional(readOnly = true)
    public TourListingDetailResponse getListingDetail(Long seq) {
        var tour = tourInfoRepository.findById(seq)
                .orElseThrow(() -> new TourInfoException(TourInfoExceptionType.NOT_FOUND_TOUR));

        tour.getActivities().size();

        var activityRes = new java.util.ArrayList<TourListingDetailResponse.Activity>();
        for (var a : tour.getActivities()) {
            var thumbs = tourActivityThumbnailRepository
                    .findAllByTourActivitySeqOrderByThumbnailOrderAsc(a.getSeq());
            var thumbUrls = new java.util.ArrayList<String>();
            for (var t : thumbs) {
                thumbUrls.add(t.getThumbnailImageUrl());
            }
            activityRes.add(new TourListingDetailResponse.Activity(
                    a.getActivityOrder(),
                    a.getActivityTitle(),
                    a.getActivityIntroduce(),
                    a.getActivityDurationMinute(),
                    thumbUrls
            ));
        }

        // photos: 별도 테이블이 없으면 썸네일 1장만 우선 세팅
        var photos = new java.util.ArrayList<String>();
        if (tour.getTourThumbnailUrl() != null) {
            photos.add(tour.getTourThumbnailUrl());
        }

        return new TourListingDetailResponse(
                tour.getSeq(),
                tour.getTourKeywords(),
                tour.getTourTitle(),
                tour.getTourIntroduce(),
                tour.getTourIncludedContent(),
                tour.getTourExcludedContent(),
                tour.getTourNotes(),
                tour.getTourLocationCode(),
                tour.getTourThumbnailUrl(),
                tour.getTourPrice(),
                tour.getMinHeadCount(),
                tour.getMaxHeadCount(),
                tour.getMeetUpAddress(),
                tour.getMeetUpLat() == null ? null : tour.getMeetUpLat().doubleValue(),
                tour.getMeetUpLng() == null ? null : tour.getMeetUpLng().doubleValue(),
                tour.getTransportServiceFlag().name(),
                tour.getPrivateFlag().name(),
                tour.getPrivatePrice(),
                tour.getAdultContentFlag().name(),
                tour.getMemo(),
                activityRes,
                photos
        );
    }

    @Transactional
    public TourListingDetailResponse updateListing(Long memberSeq, Long seq, TourCreateRequest req) {
        var guide = guideInfoRepository.findByUser_Seq(memberSeq)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));
        var tour = tourInfoRepository.findById(seq)
                .orElseThrow(() -> new TourInfoException(TourInfoExceptionType.NOT_FOUND_TOUR));

//        if (!tour.getGuide().getSeq().equals(guide.getSeq())) {
//            throw new SecurityException("본인이 등록한 리스팅만 수정할 수 있습니다.");
//        }

        Long locationCode = req.getTourLocationCode() == null ? null : req.getTourLocationCode().longValue();
        java.math.BigDecimal lat = req.getMeetUpLat() == null ? null
                : java.math.BigDecimal.valueOf(req.getMeetUpLat()).setScale(6, java.math.RoundingMode.HALF_UP);
        java.math.BigDecimal lng = req.getMeetUpLng() == null ? null
                : java.math.BigDecimal.valueOf(req.getMeetUpLng()).setScale(6, java.math.RoundingMode.HALF_UP);

        var transportYn = (req.getTransportServiceFlag() != null && "Y".equalsIgnoreCase(req.getTransportServiceFlag()))
                ? YnType.Y : YnType.N;
        var privateYn = (req.getPrivateFlag() != null && "Y".equalsIgnoreCase(req.getPrivateFlag()))
                ? YnType.Y : YnType.N;
        var adultYn = (req.getAdultContentFlag() != null && "Y".equalsIgnoreCase(req.getAdultContentFlag()))
                ? YnType.Y : YnType.N;

        String thumbnailUrl = req.getTourThumbnailUrl();
        if (thumbnailUrl == null && req.getPhotos() != null && !req.getPhotos().isEmpty()) {
            thumbnailUrl = req.getPhotos().get(0);
        }

        tour.update(
                req.getTourTitle(),
                req.getTourIntroduce(),
                req.getTourIncludedContent(),
                req.getTourExcludedContent(),
                req.getTourNotes(),
                locationCode,
                thumbnailUrl,
                req.getTourPrice(),
                req.getMinHeadCount(),
                req.getMaxHeadCount(),
                req.getMeetUpAddress(),
                lat,
                lng,
                transportYn,
                privateYn,
                req.getPrivatePrice(),
                adultYn,
                tour.getTourStatusCode(),
                tour.getApprovalAdminSeq(),
                tour.getRejectReason(),
                tour.getDeletedFlag(),
                req.getMemo()
        );

        tour.getTourKeywords().clear();
        if (req.getTourKeywords() != null && !req.getTourKeywords().isEmpty()) {
            tour.getTourKeywords().addAll(req.getTourKeywords());
        }

        for (var a : tour.getActivities()) {
            tourActivityThumbnailRepository.deleteByTourActivitySeq(a.getSeq());
        }
        tour.getActivities().clear();
        tourInfoRepository.flush();

        if (req.getActivities() != null && !req.getActivities().isEmpty()) {
            var newActs = new java.util.ArrayList<TourActivity>();
            for (var aReq : req.getActivities()) {
                var act = TourActivity.builder()
                        .tour(tour)
                        .activityOrder(aReq.getActivityOrder())
                        .activityTitle(aReq.getActivityTitle())
                        .activityIntroduce(aReq.getActivityIntroduce())
                        .activityDurationMinute(aReq.getActivityDurationMinute())
                        .deletedFlag(YnType.N)
                        .build();
                newActs.add(act);
            }
            tour.getActivities().addAll(newActs);
            tourActivityRepository.saveAll(newActs);

            int idx = 0;
            for (var aReq : req.getActivities()) {
                var parent = newActs.get(idx++);
                if (aReq.getThumbnails() == null || aReq.getThumbnails().isEmpty()) continue;
                var thumbs = new java.util.ArrayList<TourActivityThumbnail>();
                for (var th : aReq.getThumbnails()) {
                    thumbs.add(TourActivityThumbnail.builder()
                            .tourActivity(parent)
                            .thumbnailOrder(th.getThumbnailOrder())
                            .thumbnailImageUrl(th.getThumbnailImageUrl())
                            .build());
                }
                tourActivityThumbnailRepository.saveAll(thumbs);
            }
        }

        tour = tourInfoRepository.save(tour);
        tour.getActivities().size();

        var activityRes = new java.util.ArrayList<TourListingDetailResponse.Activity>();
        for (var a : tour.getActivities()) {
            var thumbs = tourActivityThumbnailRepository
                    .findAllByTourActivitySeqOrderByThumbnailOrderAsc(a.getSeq());
            var thumbUrls = new java.util.ArrayList<String>();
            for (var t : thumbs) thumbUrls.add(t.getThumbnailImageUrl());
            activityRes.add(new TourListingDetailResponse.Activity(
                    a.getActivityOrder(),
                    a.getActivityTitle(),
                    a.getActivityIntroduce(),
                    a.getActivityDurationMinute(),
                    thumbUrls
            ));
        }

        var photos = new java.util.ArrayList<String>();
        if (tour.getTourThumbnailUrl() != null) photos.add(tour.getTourThumbnailUrl());

        return new TourListingDetailResponse(
                tour.getSeq(),
                tour.getTourKeywords(),
                tour.getTourTitle(),
                tour.getTourIntroduce(),
                tour.getTourIncludedContent(),
                tour.getTourExcludedContent(),
                tour.getTourNotes(),
                tour.getTourLocationCode(),
                tour.getTourThumbnailUrl(),
                tour.getTourPrice(),
                tour.getMinHeadCount(),
                tour.getMaxHeadCount(),
                tour.getMeetUpAddress(),
                tour.getMeetUpLat() == null ? null : tour.getMeetUpLat().doubleValue(),
                tour.getMeetUpLng() == null ? null : tour.getMeetUpLng().doubleValue(),
                tour.getTransportServiceFlag().name(),
                tour.getPrivateFlag().name(),
                tour.getPrivatePrice(),
                tour.getAdultContentFlag().name(),
                tour.getMemo(),
                activityRes,
                photos
        );
    }


}
