package packup.recommend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.recommend.domain.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
