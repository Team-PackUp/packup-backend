package packup.user.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import packup.common.util.JsonUtil;
import packup.user.domain.UserInfo;

import java.util.Map;

@Getter
@Builder
public class UserInfoResponse {
    // 필요한거 여기서 추가..
    private Integer age;
    private String gender;
    private String nation;

    private String email;
    private Map<String, Object> preferCategorySeqJson;
    private String nickname;
    private String profileImagePath;

    // 가입할 때 테이블 세 군데에 insert 해 주니까 null 체크 안함
    // JSON으로 받아온 데이터를 Map으로 파싱한 후 반환해 줘야 함
    public static UserInfoResponse of(UserInfo userInfo) {

        Map<String, Object> preferCategory = JsonUtil.fromJson(
                userInfo.getPrefer().getPreferCategorySeqJson(),
                new TypeReference<Map<String, Object>>() {}
        );

        return UserInfoResponse.builder()
                .email(userInfo.getEmail())
                .gender(userInfo.getDetailInfo().getGender())
                .age(userInfo.getDetailInfo().getAge())
                .nation(userInfo.getDetailInfo().getNation())
                .preferCategorySeqJson(preferCategory)
                .nickname(userInfo.getDetailInfo().getNickname())
                .profileImagePath(userInfo.getDetailInfo().getProfileImagePath())
                .build();
    }
}
