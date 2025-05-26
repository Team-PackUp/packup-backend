package packup.tour.dto;

import lombok.Builder;
import lombok.Getter;
import packup.tour.enums.TourStatusCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 투어 상세 조회 응답 DTO
 *
 * @author SBLEE
 * @since 2025.05.16
 */
@Getter
@Builder
public class TourDetailResponse {

    /**
     * 투어 일련번호 (PK)
     */
    private Long seq;

    /**
     * 가이드 사용자 일련번호
     */
    private Long guideSeq;

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
     * 투어 소개 내용 (텍스트)
     */
    private String tourIntroduce;

    /**
     * 투어 상태 코드 (TEMP, RECRUITING, RECRUITED, READY, ONGOING, FINISHED)
     */
    private TourStatusCode tourStatusCode;

    /**
     * 상태명 라벨 (ex: 모집중, 출발대기)
     */
    private String tourStatusLabel;

    /**
     * 투어 지역명 (도시 또는 장소명)
     */
    private String tourLocation;

    /**
     * 대표 이미지 경로 (파일 경로 또는 URL)
     */
    private String titleImagePath;
}
