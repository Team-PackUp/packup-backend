package packup.guide.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import packup.guide.domain.GuideInfo;
import packup.tour.domain.TourInfo;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 * TourInfoRepository
 * - 투어 정보(TourInfo)에 대한 데이터 접근 레이어
 * </pre>
 *
 * @author SBLEE
 * @since 2025.05.16
 */
public interface GuideInfoRepository extends JpaRepository<GuideInfo, Long> {

    /**
     * 특정 가이드가 등록한 모든 투어를 조회.
     *
     * @param userSeq 가이드유저의 고유 식별자
     * @return 해당 가이드유저의 투어 리스트
     */
    List<TourInfo> findByUserSeq(Long userSeq);

    Optional<GuideInfo> findByUser_Seq(Long userSeq);

    boolean existsByUserSeq(Long userSeq);
}
