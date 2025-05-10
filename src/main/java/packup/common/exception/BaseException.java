package packup.common.exception;

public abstract class BaseException extends RuntimeException {
    public BaseException(BaseExceptionType exceptionType) {
        super(exceptionType.errorMessage());
    }
    public abstract BaseExceptionType exceptionType();
}
