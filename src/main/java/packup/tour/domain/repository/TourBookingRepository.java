package packup.tour.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import packup.tour.domain.TourBooking;

import java.util.List;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBooking, Long> {

    @Query("""
        select coalesce(sum(coalesce(b.bookingAdultCount,0) + coalesce(b.bookingKidsCount,0)), 0)
        from TourBooking b
        where b.tourSession.seq = :sessionSeq
          and b.canceledAt is null
    """)
    long countBookedHead(@Param("sessionSeq") Long sessionSeq);

    boolean existsByTourSession_SeqAndUser_SeqAndPaymentSeq(Long sessionSeq, Long userSeq, Long paymentSeq);

    boolean existsByCancelReason(String cancelReason);

    @Query("""
    select b.tourSession.seq
    from TourBooking b
    where b.user.seq in :userSeq
    """)
    List<Long> findTourSessionSeqByUserSeq(@Param("userSeq") Long userSeq);

}