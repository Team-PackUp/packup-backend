package packup.auth.exception;

import lombok.RequiredArgsConstructor;
import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class AuthException extends BaseException {

    private final AuthExceptionType exceptionType;

    public AuthException(AuthExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }


    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}