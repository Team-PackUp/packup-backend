package packup.reply.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;
import packup.reply.enums.TargetType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum ReplyExceptionType implements BaseExceptionType {

    FAIL_TO_SAVE_REPLY(BAD_REQUEST, "%s 댓글 등록에 실패 하였습니다.");

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
