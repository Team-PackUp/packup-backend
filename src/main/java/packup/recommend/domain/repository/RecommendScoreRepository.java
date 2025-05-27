package packup.recommend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.recommend.domain.Recommend;
import packup.recommend.domain.RecommendScore;
import packup.recommend.enums.ActionType;

import java.util.Optional;

public interface RecommendScoreRepository extends JpaRepository<RecommendScore, Long> {

    RecommendScore findByActionType(String actionType);
}
