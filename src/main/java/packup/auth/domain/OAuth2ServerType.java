package packup.auth.domain;

import java.util.Locale;

public enum OAuth2ServerType {

    KAKAO,
    NAVER,
    GOOGLE,
    ;

    public static OAuth2ServerType fromName(String type) {
        return OAuth2ServerType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
