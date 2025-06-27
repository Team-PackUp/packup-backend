package packup.reply.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import packup.common.enums.YnType;
import packup.reply.domain.Reply;
import packup.user.domain.UserInfo;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findAllByTargetSeqAndTargetTypeAndDeleteFlagOrderByCreatedAtDesc(Long targetSeq, String targetType, YnType ynType, Pageable pageable);
    Optional<Reply> findFirstBySeqAndDeleteFlag(Long seq, YnType deleteFlag);

    Optional<Reply> findFirstBySeqAndUserAndDeleteFlag(Long seq, UserInfo userInfo, YnType ynType);

    boolean existsByUserAndTargetSeqAndTargetTypeAndDeleteFlag(UserInfo user, Long targetSeq, String targetType, YnType deleteFlag);
}
