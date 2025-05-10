package packup.auth.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum AuthExceptionType implements BaseExceptionType {

    UNSUPPORTED_OAUTH_TYPE(BAD_REQUEST, "지원하지 않는 소셜 로그인 타입입니다."),
    NOT_FOUND_MEMBER(NOT_FOUND, "회원을 찾을 수 없습니다"),
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "인증되지 않았습니다"),
    REQUEST_EMPTY(BAD_REQUEST, "요청 값으로 NULL 을 사용할 수 없습니다."),
    INVALID_OAUTH_TYPE_CODE(BAD_REQUEST, "로그인 코드가 없습니다."),
    EXPIRED_REFRESH_TOKEN(BAD_REQUEST, "만료된 리프레쉬 토큰입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "유효하지 않은 리프레쉬 토큰입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
