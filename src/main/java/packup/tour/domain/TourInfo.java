package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.common.domain.BaseEntity;
import packup.tour.enums.TourStatusCode;
import packup.tour.enums.TourStatusCodeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <pre>
 * TourInfo (투어 정보 엔티티)
 *
 * 투어 상품의 핵심 정보를 보유하는 도메인 객체.
 * 가이드가 등록한 투어에 대해 기간, 모집 인원, 설명, 상태 등을 포함하며
 * 투어 상태 변경이나 사용자 신청 등 투어 생애주기를 관리하는 기본 단위.
 * </pre>
 *
 * <p><b>상태 흐름 예시:</b>
 * TEMP → RECRUITING → RECRUITED → READY → ONGOING → FINISHED
 * </p>
 *
 * @author SBLEE
 * @since 2025.05.12
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour_info")
@EntityListeners(AuditingEntityListener.class)
public class TourInfo extends BaseEntity {

    /**
     * 투어 일련번호 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    /**
     * 가이드 사용자 일련번호 (user_info 테이블 참조, UK)
     */
    @Column(name = "guide_seq", nullable = false)
    private Long guideSeq;

    /**
     * 최소 모집 인원
     */
    @Column(name = "min_people", nullable = false)
    private Integer minPeople;

    /**
     * 최대 모집 인원
     */
    @Column(name = "max_people", nullable = false)
    private Integer maxPeople;

    /**
     * 모집 시작일
     */
    private LocalDate applyStartDate;

    /**
     * 모집 종료일
     */
    private LocalDate applyEndDate;

    /**
     * 투어 시작 일시
     */
    private LocalDateTime tourStartDate;

    /**
     * 투어 종료 일시
     */
    private LocalDateTime tourEndDate;

    /**
     * 투어 소개 (자유 입력 설명, TEXT 컬럼)
     */
    @Column(name = "tour_introduce", columnDefinition = "TEXT")
    private String tourIntroduce;

    /**
     * 투어 상태 코드 (enum 기반, 문자열 저장)
     * - TEMP: 임시저장
     * - RECRUITING: 모집중
     * - RECRUITED: 모집완료
     * - READY: 출발대기
     * - ONGOING: 투어중
     * - FINISHED: 종료
     */
    @Convert(converter = TourStatusCodeConverter.class)
    @Column(name = "tour_status_code", length = 50)
    private TourStatusCode tourStatusCode;

    /**
     * 투어 제목
     */
    @Column(name = "tour_title", length = 255)
    private String tourTitle;

    /**
     * 투어 지역 (지역명 또는 장소명)
     */
    @Column(name = "tour_location", length = 255)
    private String tourLocation;

    /**
     * 대표 이미지 경로 (파일 시스템 또는 URL)
     */
    @Column(name = "title_image_path", length = 255)
    private String titleImagePath;

    /**
     * 수정시각
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(Integer minPeople, Integer maxPeople,
                       LocalDate applyStartDate, LocalDate applyEndDate,
                       LocalDateTime tourStartDate, LocalDateTime tourEndDate,
                       String tourTitle, String tourIntroduce, TourStatusCode tourStatusCode,
                       String tourLocation, String titleImagePath) {
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.tourStartDate = tourStartDate;
        this.tourEndDate = tourEndDate;
        this.tourTitle = tourTitle;
        this.tourIntroduce = tourIntroduce;
        this.tourStatusCode = tourStatusCode;
        this.tourLocation = tourLocation;
        this.titleImagePath = titleImagePath;
    }
}
