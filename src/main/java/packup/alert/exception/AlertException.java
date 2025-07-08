package packup.alert.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class AlertException extends BaseException {
    private final AlertExceptionType exceptionType;

    public AlertException(AlertExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
