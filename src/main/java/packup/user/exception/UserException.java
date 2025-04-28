package packup.user.exception;

import lombok.RequiredArgsConstructor;
import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

@RequiredArgsConstructor
public class UserException extends BaseException {

    private final UserExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}