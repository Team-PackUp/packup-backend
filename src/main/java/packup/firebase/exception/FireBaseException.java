package packup.firebase.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class FireBaseException extends BaseException {
    private final FireBaseExceptionType exceptionType;

    public FireBaseException(FireBaseExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
