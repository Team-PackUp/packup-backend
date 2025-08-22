package packup.tour.domain.repositoroy;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.tour.domain.TourInfo;
import packup.tour.domain.TourSession;

import java.util.List;

public interface TourSessionRepository extends JpaRepository<TourSession, Long> {
    List<TourInfo> findByTourSeq(Long TourSeq);
}
