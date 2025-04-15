package packup.auth.infra.oauth.kakao.dto;

import lombok.Data;

@Data
public class KakaoMemberResponse {
    private KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {
        private String email;
        private Profile profile;
    }

    @Data
    public static class Profile {
        private String nickname;
        private String profileImageUrl;
    }
}