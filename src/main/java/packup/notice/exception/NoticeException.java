package packup.notice.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class NoticeException extends BaseException {

    private final NoticeExceptionType exceptionType;

    public NoticeException(NoticeExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}