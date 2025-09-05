package packup.tour.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourActivity;

public interface TourActivityRepository extends JpaRepository<TourActivity, Long> {
}
