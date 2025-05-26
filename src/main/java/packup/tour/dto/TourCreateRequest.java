package packup.tour.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import packup.tour.enums.TourStatusCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 투어 등록 요청 DTO
 *
 * @author SBLEE
 * @since 2025.05.16
 */
@Getter
@Setter
public class TourCreateRequest {

    /**
     * 최소 모집 인원
     */
    @NotNull
    private Integer minPeople;

    /**
     * 최대 모집 인원
     */
    @NotNull
    private Integer maxPeople;

    /**
     * 모집 시작일 (yyyy-MM-dd)
     */
    @NotNull
    private LocalDate applyStartDate;

    /**
     * 모집 종료일 (yyyy-MM-dd)
     */
    @NotNull
    private LocalDate applyEndDate;

    /**
     * 투어 시작일시 (yyyy-MM-dd'T'HH:mm:ss)
     */
    @NotNull
    private LocalDateTime tourStartDate;

    /**
     * 투어 종료일시 (yyyy-MM-dd'T'HH:mm:ss)
     */
    @NotNull
    private LocalDateTime tourEndDate;

    /**
     * 투어 소개
     */
    @NotBlank
    private String tourIntroduce;

    /**
     * 투어 상태 코드
     * - TEMP: 임시저장
     * - RECRUITING: 모집중
     * - RECRUITED: 모집완료
     * - READY: 출발대기
     * - ONGOING: 투어중
     * - FINISHED: 종료
     */
    @NotNull
    private TourStatusCode tourStatusCode;

    /**
     * 투어 지역
     */
    @NotBlank
    private String tourLocation;

    /**
     * 대표 이미지 경로
     */
    @NotBlank
    private String titleImagePath;
}