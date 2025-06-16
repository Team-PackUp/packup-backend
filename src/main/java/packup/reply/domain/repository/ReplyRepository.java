package packup.reply.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.common.enums.YnType;
import packup.reply.domain.Reply;
import packup.reply.enums.TargetType;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<List<Reply>> findAllByTargetSeqAndTargetTypeAndDeleteFlagOrderByCreatedAtDesc(Long targetSeq, TargetType targetType, YnType ynType);

}
