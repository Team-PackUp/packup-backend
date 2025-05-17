package packup.chat.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum ChatExceptionType implements BaseExceptionType {

    NOT_FOUND_CHAT_ROOM(BAD_REQUEST, "채팅 방을 찾을 수 없습니다"),
    NOT_FOUND_MEMBER(NOT_FOUND, "회원을 찾을 수 없습니다"),
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "인증되지 않았습니다"),
    ALREADY_PARTICIPATION(UNAUTHORIZED, "이미 참여한 회원 입니다"),
    REQUEST_EMPTY(BAD_REQUEST, "요청 값으로 NULL 을 사용할 수 없습니다."),
    FAIL_TO_SAVE_MESSAGE(BAD_REQUEST, "채팅 발송에 실패 하였습니다"),
    ABNORMAL_ACCESS(BAD_REQUEST, "비정상적인 접근입니다"),
    FAIL_TO_PUSH_FCM(BAD_REQUEST, "FCM 발송 실패"),
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
