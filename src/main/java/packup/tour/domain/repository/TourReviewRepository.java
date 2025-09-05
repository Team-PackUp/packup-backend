package packup.tour.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourReview;

public interface TourReviewRepository extends JpaRepository<TourReview, Long> {
}
