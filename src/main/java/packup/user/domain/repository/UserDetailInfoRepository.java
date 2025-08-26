package packup.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.UserPrefer;
import packup.user.dto.UserProfileImageResponse;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDetailInfoRepository extends JpaRepository<UserDetailInfo, Long> {
    Optional<UserDetailInfo> findByUser(UserInfo user);

    boolean existsByUserNotAndNickname(UserInfo user, String nickname);

    @Query("select new packup.user.dto.UserProfileImageResponse(u.seq, d.profileImagePath) " +
            "from UserInfo u left join u.detailInfo d " +
            "where u.seq = :seq")
    Optional<UserProfileImageResponse> findProfileImageBySeq(Long seq);


    @Query("select new packup.user.dto.UserProfileImageResponse(d.user.seq, d.profileImagePath) " +
            "from UserDetailInfo d where d.user.seq in :userSeq")
    List<UserProfileImageResponse> findProfileImagesByUserSeqIn(Collection<Long> userSeq);


}
