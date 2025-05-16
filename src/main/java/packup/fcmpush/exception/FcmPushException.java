package packup.fcmpush.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class FcmPushException extends BaseException {
    private final FcmPushExceptionType exceptionType;

    public FcmPushException(FcmPushExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
