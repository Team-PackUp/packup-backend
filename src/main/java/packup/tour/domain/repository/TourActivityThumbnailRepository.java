package packup.tour.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourActivityThumbnail;

public interface TourActivityThumbnailRepository extends JpaRepository<TourActivityThumbnail, Long> {
}
