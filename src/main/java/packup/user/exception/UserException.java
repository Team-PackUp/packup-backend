package packup.user.exception;

import lombok.RequiredArgsConstructor;
import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class UserException extends BaseException {

    private final UserExceptionType exceptionType;

    public UserException(UserExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}