package packup.tour.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import packup.guide.domain.GuideInfo;
import packup.guide.dto.GuideInfoResponse;
import packup.tour.domain.TourInfo;
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
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TourInfoResponse {

    /**
     * 투어 일련번호 (PK)
     */
    private Long seq;

    /**
     * 가이드 사용자 일련번호
     */
    private GuideInfoResponse guide;

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
     * 투어 제목 (텍스트)
     */
    private String tourTitle;

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

    /**
     * 등록시각
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정시각
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static TourInfoResponse from(TourInfo tourInfo) {
        GuideInfo guide = tourInfo.getGuide();

        GuideInfoResponse guideDto = GuideInfoResponse.builder()
                .seq(guide.getSeq())
                .userSeq(guide.getUser().getSeq())
                .guideName(guide.getGuideName())
                .telNumber(guide.getTelNumber())
                .telNumber2(guide.getTelNumber2())
                .languages(guide.getLanguages())
                .guideIntroduce(guide.getGuideIntroduce())
                .guideRating(guide.getGuideRating())
                .createdAt(guide.getCreatedAt())
                .updatedAt(guide.getUpdatedAt())
                .build();

        return TourInfoResponse.builder()
                .seq(tourInfo.getSeq())
                .guide(guideDto)
                .minPeople(tourInfo.getMinPeople())
                .maxPeople(tourInfo.getMaxPeople())
                .applyStartDate(tourInfo.getApplyStartDate())
                .applyEndDate(tourInfo.getApplyEndDate())
                .tourStartDate(tourInfo.getTourStartDate())
                .tourEndDate(tourInfo.getTourEndDate())
                .tourTitle(tourInfo.getTourTitle())
                .tourIntroduce(tourInfo.getTourIntroduce())
                .tourStatusCode(tourInfo.getTourStatusCode())
                .tourStatusLabel(tourInfo.getTourStatusCode().getLabel())
                .tourLocation(tourInfo.getTourLocation())
                .titleImagePath(tourInfo.getTitleImagePath())
                .createdAt(tourInfo.getCreatedAt())
                .updatedAt(tourInfo.getUpdatedAt())
                .build();
    }
}
