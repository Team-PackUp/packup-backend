package packup.faq.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.common.enums.YnType;
import packup.faq.domain.Faq;

import java.util.List;
import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    Optional<List<Faq>> findAllByDeleteFlag(YnType deleteFlag);
}
