package packup.common.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.common.domain.CommonCode;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    // 중복이면 2개 나올 듯
    Optional<CommonCode> findByCodeName(String codeName);
}
