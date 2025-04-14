package packup.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

@RequiredArgsConstructor
public enum AuthExceptionType implements BaseExceptionType {

    INVALID_TOKEN(BAD_REQUEST, "토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레쉬 토큰이 만료되었습니다.")
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
