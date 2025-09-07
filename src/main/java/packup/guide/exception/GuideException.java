package packup.guide.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class GuideException extends BaseException {
    private final GuideExceptionType exceptionType;

    public GuideException(GuideExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
