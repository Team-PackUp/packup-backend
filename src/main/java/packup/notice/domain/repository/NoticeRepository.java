package packup.notice.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import packup.common.enums.YnType;
import packup.notice.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByDeleteFlagOrderByCreatedAtDesc(YnType ynType, Pageable page);
}
