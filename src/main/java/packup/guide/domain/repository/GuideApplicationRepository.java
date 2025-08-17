package packup.guide.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.guide.domain.GuideApplication;

public interface GuideApplicationRepository extends JpaRepository<GuideApplication, Long> {
}
