package packup.tour.dto.tourInfo;

import lombok.Getter;
import lombok.Setter;
import packup.common.enums.YnType;
import packup.tour.enums.TourStatusCode;

import java.math.BigDecimal;

/**
 * 투어 수정 요청 DTO
 *
 * @author SBLEE
 * @since 2025.08.16
 */
@Getter
@Setter
public class TourInfoUpdateRequest {

    /** 수정 대상 투어 일련번호 (PK) */
    private Long seq;

    /** 투어 제목 */
    private String tourTitle;

    /** 투어 소개 */
    private String tourIntroduce;

    /** 투어 포함 콘텐츠 */
    private String tourIncludedContent;

    /** 투어 미포함 콘텐츠 */
    private String tourExcludedContent;

    /** 투어 참고사항 */
    private String tourNotes;

    /** 투어 지역 코드 */
    private Long tourLocationCode;

    /** 투어 섬네일 URL */
    private String tourThumbnailUrl;

    /** 투어 가격 */
    private Long tourPrice;

    /** 최소 인원 수 */
    private Integer minHeadCount;

    /** 최대 인원 수 */
    private Integer maxHeadCount;

    /** 모임 주소 */
    private String meetUpAddress;

    /** 모임 위도좌표 */
    private BigDecimal meetUpLat;

    /** 모임 경도좌표 */
    private BigDecimal meetUpLng;

    /** 차량 운송 여부 (Y/N) */
    private YnType transportServiceFlag;

    /** 프라이빗 제공 여부 (Y/N) */
    private YnType privateFlag;

    /** 프라이빗 가격 */
    private Long privatePrice;

    /** 성인 콘텐츠 포함 여부 (Y/N) */
    private YnType adultContentFlag;

    /** 투어 상태 코드 */
    private TourStatusCode tourStatusCode;

    /** 승인 관리자 식별번호 */
    private Integer approvalAdminSeq;

    /** 반려사유 */
    private String rejectReason;

    /** 삭제 여부 (Y/N) */
    private YnType deletedFlag;

    /** 가이드 관리용 메모 */
    private String memo;
}
