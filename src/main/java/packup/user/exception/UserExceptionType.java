package packup.user.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum UserExceptionType implements BaseExceptionType {


    NOT_FOUND_MEMBER(NOT_FOUND, "회원을 찾을 수 없습니다"),
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
