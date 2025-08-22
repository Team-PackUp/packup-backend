package packup.tour.dto.tourInfo;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import packup.common.enums.YnType;
import packup.tour.enums.TourStatusCode;

import java.math.BigDecimal;

/**
 * 투어 등록 요청 DTO
 *
 * @author SBLEE
 * @since 2025.08.16
 */
@Getter
@Setter
public class TourInfoCreateRequest {

    /** 투어 제목 */
    @NotBlank
    private String tourTitle;

    /** 투어 소개 (TEXT) */
    @NotBlank
    private String tourIntroduce;

    /** 투어 포함 콘텐츠 (TEXT) */
    private String tourIncludedContent;

    /** 투어 미포함 콘텐츠 (TEXT) */
    private String tourExcludedContent;

    /** 투어 참고사항 (최대 255자 권장) */
    private String tourNotes;

    /** 투어 지역 코드 */
    @NotNull
    private Long tourLocationCode;

    /** 투어 섬네일 URL */
    @NotBlank
    private String tourThumbnailUrl;

    /** 투어 가격 (BIGINT) */
    @NotNull @Positive
    private Long tourPrice;

    /** 최소 인원 수 (SMALLINT) */
    @NotNull @Positive
    private Integer minHeadCount;

    /** 최대 인원 수 (SMALLINT) */
    @NotNull @Positive
    private Integer maxHeadCount;

    /** 모임 주소 (TEXT) */
    @NotBlank
    private String meetUpAddress;

    /** 모임 위도좌표 (numeric(9,6)) */
    @NotNull
    @Digits(integer = 3, fraction = 6)   // 예: 37.566535
    private BigDecimal meetUpLat;

    /** 모임 경도좌표 (numeric(9,6)) */
    @NotNull
    @Digits(integer = 3, fraction = 6)   // 예: 126.977969
    private BigDecimal meetUpLng;

    /** 차량 운송 여부 (Y/N) */
    @NotNull
    private YnType transportServiceFlag = YnType.N;

    /** 프라이빗 제공여부 (Y/N) */
    @NotNull
    private YnType privateFlag = YnType.N;

    /** 프라이빗 가격 (프라이빗 제공시 필수) */
    private Long privatePrice;

    /** 성인 콘텐츠 포함 여부 (Y/N) */
    @NotNull
    private YnType adultContentFlag = YnType.N;

    /**
     * 투어 상태 코드
     * - TEMP, RECRUITING, RECRUITED, READY, ONGOING, FINISHED
     */
    @NotNull
    private TourStatusCode tourStatusCode;

}
