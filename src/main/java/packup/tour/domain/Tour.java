package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;

/**
 * 관광(Tour) 도메인 엔티티
 * - 투어 상품에 대한 핵심 정보를 보유
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour")
public class Tour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;  // 투어 일련번호 (PK)

    @Column(name = "guide_seq", nullable = false, unique = true)
    private Long guideSeq;  // 가이드(user_info) 일련번호

    @Column(name = "minimum_people", nullable = false)
    private Integer minimumPeople;  // 최소 모집 인원

    @Column(name = "max_people", nullable = false)
    private Integer maxPeople;  // 최대 모집 인원

    @Embedded
    private RecruitmentPeriod recruitmentPeriod;  // 모집 기간 (시작일 ~ 종료일)

    @Embedded
    private TourPeriod tourPeriod;  // 관광 기간 (시작 일시 ~ 종료 일시)

    @Column(name = "tour_introduce", columnDefinition = "TEXT")
    private String tourIntroduce;  // 투어 설명

    @Column(name = "location", length = 255)
    private String location;  // 관광 지역

    @Column(name = "title_image_path", length = 255)
    private String titleImagePath;  // 대표 이미지 경로
}
