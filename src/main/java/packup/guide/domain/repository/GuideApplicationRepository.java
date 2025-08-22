package packup.guide.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.guide.domain.GuideApplication;

import java.util.Optional;

public interface GuideApplicationRepository extends JpaRepository<GuideApplication, Long> {

    Optional<GuideApplication> findTopByUserSeqOrderByCreatedAtDesc(Long userSeq);
}
