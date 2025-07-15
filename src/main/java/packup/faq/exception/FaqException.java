package packup.faq.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class FaqException extends BaseException {
    private final FaqExceptionType exceptionType;

    public FaqException(FaqExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
