package packup.fcmpush.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum FcmPushExceptionType implements BaseExceptionType {

    FAIL_TO_SEND_MESSAGE(BAD_REQUEST, "FCM 발송에 실패 하였습니다"),
    FAIL_TO_LOAD_CONFIG(BAD_REQUEST, "서버에 문제가 발생 하였습니다."),
    INVALID_TOKEN_OWNER(FORBIDDEN, "다른 사용자의 FCM 토큰입니다."),
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
