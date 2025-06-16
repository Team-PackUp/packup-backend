package packup.reply.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class ReplyException extends BaseException {
    private final ReplyExceptionType exceptionType;

    public ReplyException(ReplyExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
