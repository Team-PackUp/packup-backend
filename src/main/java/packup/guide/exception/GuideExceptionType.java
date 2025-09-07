package packup.guide.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequiredArgsConstructor
public enum GuideExceptionType implements BaseExceptionType {

    NOT_FOUND_GUIDE(FORBIDDEN, "해당 가이드는 존재하지 않습니다."),
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
