package packup.reply.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;
import packup.reply.enums.TargetType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum ReplyExceptionType implements BaseExceptionType {

    FAIL_TO_SAVE_REPLY(INTERNAL_SERVER_ERROR, "%s 댓글 등록에 실패 하였습니다."),
    ABNORMAL_ACCESS(BAD_REQUEST, "비정상적인 접근입니다"), 
    NOT_FOUND_MEMBER(NOT_FOUND, "해당 회원을 찾을 수 없습니다"),
    NOT_FOUND_TOUR(NOT_FOUND, "해당 관광을 찾을 수 없습니다"),
    NOT_FOUND_GUIDE(NOT_FOUND, "해당 가이드를 찾을 수 없습니다"),
    INVALID_REPLY_TYPE(FORBIDDEN, "유효하지 않은 댓글 TYPE입니다.");

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

    public String withType(TargetType targetType) {
        return String.format(errorMessage, targetType.getValue());
    }
}
