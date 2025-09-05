package packup.tour.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.tour.domain.TourSession;

import java.time.LocalDateTime;
import java.util.List;

public interface TourSessionRepository extends JpaRepository<TourSession, Long> {

    // 투어별 세션 조회
    List<TourSession> findAllByTour_SeqOrderBySessionStartTimeAsc(Long tourSeq);

    // 시간 겹침( [start, end) ) 조회 — 소프트삭제 제외(N) 조건 포함
    @Query("""
        select s
          from TourSession s
         where s.tour.seq = :tourSeq
           and s.deletedFlag = packup.common.enums.YnType.N
           and s.sessionStartTime < :end
           and s.sessionEndTime   > :start
        """)
    List<TourSession> findOverlapping(
            @Param("tourSeq") Long tourSeq,
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end
    );
}
