package packup.tour.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;
import packup.tour.domain.TourInfo;
import packup.tour.enums.TourStatusCode;

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

    Page<TourInfo> findByGuide_User_SeqAndDeletedFlag(
            Long memberSeq,
            YnType deletedFlag,
            Pageable pageable
    );





    /// /////////
    /**
     * 특정 가이드가 등록한 모든 투어를 조회.
     *
     * @param guideSeq 가이드의 고유 식별자
     * @return 해당 가이드의 투어 리스트
     */
    List<TourInfo> findByGuideSeq(Long guideSeq);

    Page<TourInfo> findByGuide_User_Seq(Long guideUserSeq, Pageable pageable);

    @Query("SELECT t FROM TourInfo t WHERE t.tourStatusCode = :tourStatusCode")
    Page<TourInfo> findFilteredTours(@Param("tourStatusCode") TourStatusCode tourStatusCode, Pageable pageable);

    @Query("""
           select t.seq
           from   TourInfo t
           """)
    List<Long> findAllBetweenApplyDate(@Param("today") LocalDate today);

    @Query("""
    select t
    from   TourInfo t
    order  by t.seq desc
""")
    Page<TourInfo> findLatest(@Param("today") LocalDate today,
                              Pageable pageable);   // PageRequest.of(0, N, Sort.by(DESC, "seq"))


    @Query("SELECT t FROM TourInfo t WHERE t.tourLocationCode = :regionCode")
    Page<TourInfo> findFilteredToursByRegion(@Param("regionCode") String regionCode, Pageable pageable);

    List<TourInfo> findAllByGuide(GuideInfo guideInfo);

    @Query("""
    select t
    from TourInfo t
    where t.seq in :tourSeq
    and t.deletedFlag = 'N'
    """)
    Page<TourInfo> findAllByTourSeq(@Param("tourSeq") List<Long> tourSeq, Pageable pageable);
}
