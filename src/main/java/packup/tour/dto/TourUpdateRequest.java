package packup.tour.dto;

import lombok.Getter;
import lombok.Setter;
import packup.tour.enums.TourStatusCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 투어 수정 요청 DTO
 *
 * @author SBLEE
 * @since 2025.05.16
 */
@Getter
@Setter
public class TourUpdateRequest {

    /**
     * 수정 대상 투어 일련번호 (PK)
     */
    private Long seq;

    /**
     * 최소 모집 인원
     */
    private Integer minPeople;

    /**
     * 최대 모집 인원
     */
    private Integer maxPeople;

    /**
     * 모집 시작일 (yyyy-MM-dd)
     */
    private LocalDate applyStartDate;

    /**
     * 모집 종료일 (yyyy-MM-dd)
     */
    private LocalDate applyEndDate;

    /**
     * 투어 시작일시 (yyyy-MM-dd'T'HH:mm:ss)
     */
    private LocalDateTime tourStartDate;

    /**
     * 투어 종료일시 (yyyy-MM-dd'T'HH:mm:ss)
     */
    private LocalDateTime tourEndDate;

    /**
     * 투어 소개
     */
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
    private TourStatusCode tourStatusCode;

    /**
     * 투어 지역
     */
    private String tourLocation;

    /**
     * 대표 이미지 경로
     */
    private String titleImagePath;
}
