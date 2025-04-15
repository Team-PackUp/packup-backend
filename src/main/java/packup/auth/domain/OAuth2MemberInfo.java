package packup.auth.domain;

public record OAuth2MemberInfo (
        String email,
        String nickname,
        String profileImageUrl,
        String oauthId,
        String provider
) {}