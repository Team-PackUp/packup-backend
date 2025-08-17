package packup.tour.domain.repositoroy;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourBooking;

public interface TourBookingRepository extends JpaRepository<TourBooking, Long> {
}
