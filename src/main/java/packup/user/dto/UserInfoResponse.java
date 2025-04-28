package packup.user.dto;

import lombok.Builder;
import lombok.Getter;
import packup.user.domain.UserInfo;

@Getter
@Builder
public class UserInfoResponse {
    // 필요한거 여기서 추가..
    String email;
    String preferCategorySeqJson;
    String nickname;

    // 가입할 때 테이블 세 군데에 insert 해 주니까 null 체크 안함
    public static UserInfoResponse of(UserInfo userInfo) {
        return UserInfoResponse.builder()
                .email(userInfo.getEmail())
                .preferCategorySeqJson(userInfo.getPrefer().getPreferCategorySeqJson())
                .nickname(userInfo.getDetailInfo().getNickname())
                .build();
    }
}
