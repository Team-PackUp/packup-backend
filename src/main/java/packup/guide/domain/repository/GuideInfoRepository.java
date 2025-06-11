package packup.guide.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourInfo;

import java.util.List;

/**
 * <pre>
 * TourInfoRepository
 * - 투어 정보(TourInfo)에 대한 데이터 접근 레이어
 * </pre>
 *
 * @author SBLEE
 * @since 2025.05.16
 */
public interface GuideInfoRepository extends JpaRepository<TourInfo, Long> {

    /**
     * 특정 가이드가 등록한 모든 투어를 조회.
     *
     * @param guideSeq 가이드의 고유 식별자
     * @return 해당 가이드의 투어 리스트
     */
    List<TourInfo> findByGuideSeq(Long guideSeq);
}
