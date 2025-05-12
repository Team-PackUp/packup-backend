package packup.tour.repositoroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.tour.domain.TourInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * <pre>
 * TourInfoRepository
 * - 투어 정보(TourInfo)에 대한 데이터 접근 레이어
 * </pre>
 *
 * @author SBLEE
 * @since 2025.05.12
 */
public interface TourInfoRepository extends JpaRepository<TourInfo, Long> {

    /**
     * 특정 가이드가 등록한 모든 투어를 조회.
     *
     * @param guideSeq 가이드의 고유 식별자
     * @return 해당 가이드의 투어 리스트
     */
    List<TourInfo> findByGuideSeq(Long guideSeq);

    /**
     * 현재 날짜 기준으로 모집 기간 내에 있는 투어를 조회.
     *
     * @param today 기준 날짜 (보통 LocalDate.now())
     * @return 현재 모집 중인 투어 리스트
     */
    @Query("SELECT t FROM tour_info t WHERE :today BETWEEN t.recruitmentPeriod.startDate AND t.recruitmentPeriod.endDate")
    List<TourInfo> findRecruitingTours(@Param("today") LocalDate today);
}
