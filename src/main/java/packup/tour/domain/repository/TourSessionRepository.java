package packup.tour.domain.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.common.enums.YnType;
import packup.tour.domain.TourSession;
import packup.tour.dto.tourSession.TourSessionOpenResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TourSessionRepository extends JpaRepository<TourSession, Long> {

    List<TourSession> findAllByTour_SeqAndDeletedFlagOrderBySessionStartTimeAsc(
            Long tourSeq, YnType deletedFlag
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ts from TourSession ts where ts.seq = :seq")
    Optional<TourSession> findByIdForUpdate(@Param("seq") Long seq);


    @Query("""
        select s
          from TourSession s
         where s.tour.seq = :tourSeq
           and s.sessionStartTime < :end
           and s.sessionEndTime   > :start
        """)
    List<TourSession> findOverlapping(
            @Param("tourSeq") Long tourSeq,
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end,
            @Param("active") YnType active
    );
    @Query("""
    select new packup.tour.dto.tourSession.TourSessionOpenResponse(
        ts.seq,
        t.seq,
        ts.sessionStartTime,
        ts.sessionEndTime,
        ts.sessionStatusCode,
        ts.cancelledAt,
        ts.maxParticipants,
        coalesce((
            select sum(
                cast(
                    (coalesce(b.bookingAdultCount,0) + coalesce(b.bookingKidsCount,0))
                as long)
            )
            from TourBooking b
            where b.tourSession = ts
              and b.canceledAt is null
        ), 0L)
    )
    from TourSession ts
      join ts.tour t
    where t.seq = :tourSeq
      and ts.deletedFlag = :active
      and t.deletedFlag = :active
      and ts.sessionStatusCode = :openCode
      and ts.sessionStartTime >= coalesce(:from, ts.sessionStartTime)
    order by ts.sessionStartTime asc
    """)
    List<TourSessionOpenResponse> findOpenSessionsWithBookedCount(
            @Param("tourSeq") Long tourSeq,
            @Param("from") LocalDateTime from,
            @Param("openCode") Integer openCode,
            @Param("active") YnType active
    );


}
