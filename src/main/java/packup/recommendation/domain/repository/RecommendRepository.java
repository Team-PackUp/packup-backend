package packup.recommendation.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.recommendation.domain.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
