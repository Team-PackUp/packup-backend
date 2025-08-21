package packup.tour.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;
import packup.guide.dto.GuideInfoResponse;
import packup.tour.domain.TourInfo;
import packup.tour.enums.TourStatusCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 투어 상세 조회 응답 DTO
 *
 * @author SBLEE
 * @since 2025.08.16
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TourInfoResponse {

    /** 투어 정보 식별번호 (PK) */
    private Long seq;

    /** 가이드 정보 */
    private GuideInfoResponse guide;

    /** 투어 제목 */
    private String tourTitle;

    /** 투어 소개 (TEXT) */
    private String tourIntroduce;

    /** 투어 포함 콘텐츠 (TEXT) */
    private String tourIncludedContent;

    /** 투어 미포함 콘텐츠 (TEXT) */
    private String tourExcludedContent;

    /** 투어 참고사항 */
    private String tourNotes;

    /** 투어 지역 코드 */
    private Long tourLocationCode;

    /** 투어 섬네일 URL */
    private String tourThumbnailUrl;

    /** 투어 가격 (BIGINT) */
    private Long tourPrice;

    /** 최소 인원 수 (SMALLINT) */
    private Integer minHeadCount;

    /** 최대 인원 수 (SMALLINT) */
    private Integer maxHeadCount;

    /** 모임 주소 (TEXT) */
    private String meetUpAddress;

    /** 모임 위도좌표 (numeric(9,6)) */
    private BigDecimal meetUpLat;

    /** 모임 경도좌표 (numeric(9,6)) */
    private BigDecimal meetUpLng;

    /** 차량 운송 여부 (Y/N) */
    private YnType transportServiceFlag;

    /** 프라이빗 제공 여부 (Y/N) */
    private YnType privateFlag;

    /** 프라이빗 가격 (BIGINT) */
    private Long privatePrice;

    /** 성인 콘텐츠 포함 여부 (Y/N) */
    private YnType adultContentFlag;

    /** 투어 상태 코드 */
    private TourStatusCode tourStatusCode;

    /** 상태 라벨 (예: 모집중, 출발대기) */
    private String tourStatusLabel;

    /** 승인 관리자 식별번호 */
    private Integer approvalAdminSeq;

    /** 반려사유 (TEXT) */
    private String rejectReason;

    /** 삭제 여부 (Y/N) */
    private YnType deletedFlag;

    /** 가이드 관리용 메모 */
    private String memo;

    /** 등록일시 */
    private LocalDateTime createdAt;

    /** 수정일시 */
    private LocalDateTime updatedAt;

    /**
     * 엔티티 -> DTO 매핑
     */
    public static TourInfoResponse from(TourInfo tourInfo) {
        GuideInfo guide = tourInfo.getGuide();

        GuideInfoResponse guideDto = GuideInfoResponse.builder()
                .seq(guide.getSeq())
                .userSeq(guide.getUser().getSeq())
//                .guideName(guide.getGuideName())
//                .telNumber(guide.getTelNumber())
//                .telNumber2(guide.getTelNumber2())
//                .languages(guide.getLanguages())
                .guideIntroduce(guide.getGuideIntroduce())
//                .guideRating(guide.getGuideRating())
//                .guideAvatarPath(guide.getGuideAvatarPath())
                .createdAt(guide.getCreatedAt())
                .updatedAt(guide.getUpdatedAt())
                .build();

        TourStatusCode status = tourInfo.getTourStatusCode();

        return TourInfoResponse.builder()
                .seq(tourInfo.getSeq())
                .guide(guideDto)
                .tourTitle(tourInfo.getTourTitle())
                .tourIntroduce(tourInfo.getTourIntroduce())
                .tourIncludedContent(tourInfo.getTourIncludedContent())
                .tourExcludedContent(tourInfo.getTourExcludedContent())
                .tourNotes(tourInfo.getTourNotes())
                .tourLocationCode(tourInfo.getTourLocationCode())
                .tourThumbnailUrl(tourInfo.getTourThumbnailUrl())
                .tourPrice(tourInfo.getTourPrice())
                .minHeadCount(tourInfo.getMinHeadCount())
                .maxHeadCount(tourInfo.getMaxHeadCount())
                .meetUpAddress(tourInfo.getMeetUpAddress())
                .meetUpLat(tourInfo.getMeetUpLat())
                .meetUpLng(tourInfo.getMeetUpLng())
                .transportServiceFlag(tourInfo.getTransportServiceFlag())
                .privateFlag(tourInfo.getPrivateFlag())
                .privatePrice(tourInfo.getPrivatePrice())
                .adultContentFlag(tourInfo.getAdultContentFlag())
                .tourStatusCode(status)
                .tourStatusLabel(status != null ? status.getLabel() : null)
                .approvalAdminSeq(tourInfo.getApprovalAdminSeq())
                .rejectReason(tourInfo.getRejectReason())
                .deletedFlag(tourInfo.getDeletedFlag())
                .memo(tourInfo.getMemo())
                .createdAt(tourInfo.getCreatedAt())
                .updatedAt(tourInfo.getUpdatedAt())
                .build();
    }
}
