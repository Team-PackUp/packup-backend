package packup.chat.exception;

import lombok.RequiredArgsConstructor;
import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

@RequiredArgsConstructor
public class ChatException extends BaseException {

    private final ChatExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}