package packup.reply.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.reply.domain.ReplyFile;

public interface ReplyFileRepository extends JpaRepository<ReplyFile, Long> {
}
