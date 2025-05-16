package packup.notification.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class NotificationException extends BaseException {
    private final NotificationExceptionType exceptionType;

    public NotificationException(NotificationExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
