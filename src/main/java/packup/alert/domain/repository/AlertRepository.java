package packup.alert.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import packup.alert.domain.Alert;
import packup.common.enums.YnType;

import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Page<Alert> findAllByUserSeqAndReadFlagOrderByCreatedAtDesc(Long userSeq, YnType ynType, Pageable page);

    Optional<Alert> findFirstBySeqAndUserSeq(Long alertSeq, Long memeberId);
}
