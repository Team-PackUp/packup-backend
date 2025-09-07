package packup.tour.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourActivityThumbnail;

import java.util.List;

public interface TourActivityThumbnailRepository extends JpaRepository<TourActivityThumbnail, Long> {
    void deleteByTourActivitySeq(Long activitySeq);
    List<TourActivityThumbnail> findAllByTourActivitySeqOrderByThumbnailOrderAsc(Long activitySeq);
}
