package packup.chat.exception;

import lombok.RequiredArgsConstructor;
import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class ChatException extends BaseException {

    private final ChatExceptionType exceptionType;

    public ChatException(ChatExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}