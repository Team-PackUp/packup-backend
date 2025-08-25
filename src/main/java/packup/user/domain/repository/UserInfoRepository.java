package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;
import packup.user.dto.UserPushTarget;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);

    @Query("""
    select u.seq
    from UserInfo u
    where u.seq in :seq
      and u.withdrawFlag = :withdrawFlag
""")
    List<Long> findActiveUserSeq(@Param("seq") Collection<Long> seq,
                                  @Param("withdrawFlag") YnType withdrawFlag);

    @Query("""
      select new packup.user.dto.UserPushTarget(
        u.seq,
        d.nickname,
        d.profileImagePath,
        t.fcmToken
      )
      from UserInfo u
      left join u.detailInfo d
      left join UserFcmToken t
        on t.user = u and t.activeFlag = :active
      where u.withdrawFlag = :withdraw and u.seq in :seq
    """)
    List<UserPushTarget> findPushTargets(@Param("seq") List<Long> seq, YnType active, YnType withdraw);

    @Query("""
      select u from UserInfo u
      left join fetch u.detailInfo d
      where u.seq = :seq
    """)
    Optional<UserInfo> findBySeqWithDetail(@Param("seq") Long seq);

    @Query("select u from UserInfo u left join fetch u.detailInfo where u.seq in :seq")
    List<UserInfo> findAllWithDetailBySeqIn(@Param("seq") Collection<Long> seq);
}
