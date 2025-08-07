package packup.user.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import packup.common.enums.YnType;
import packup.common.util.JsonUtil;
import packup.user.domain.UserInfo;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class UserInfoResponse {
    // 필요한거 여기서 추가..
    private Integer age;
    private String gender;
    private String nation;
    private String joinType;

    private String email;
    private String language;
    private List<String> preferCategorySeqJson;
    private String nickname;
    private String profileImagePath;

    private YnType marketingFlag;
    private YnType pushFlag;

    // 가입할 때 테이블 세 군데에 insert 해 주니까 null 체크 안함
    // JSON으로 받아온 데이터를 Map으로 파싱한 후 반환해 줘야 함
    public static UserInfoResponse of(UserInfo userInfo) {

        List<String> preferCategory = JsonUtil.fromJson(
                userInfo.getPrefer().getPreferCategorySeqJson(),
                new TypeReference<List<String>>() {}
        );

        return UserInfoResponse.builder()
                .email(userInfo.getEmail())
                .gender(userInfo.getDetailInfo().getGender())
                .age(userInfo.getDetailInfo().getAge())
                .nation(userInfo.getDetailInfo().getNation())
                .joinType(userInfo.getJoinType())
                .language(userInfo.getDetailInfo().getLanguage())
                .preferCategorySeqJson(preferCategory)
                .nickname(userInfo.getDetailInfo().getNickname())
                .profileImagePath(userInfo.getDetailInfo().getProfileImagePath())
                .marketingFlag(userInfo.getDetailInfo().getMarketingFlag())
                .pushFlag(userInfo.getDetailInfo().getPushFlag())
                .build();
    }
}
