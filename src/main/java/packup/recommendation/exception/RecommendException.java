package packup.recommendation.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class RecommendException extends BaseException {

    private final RecommendExceptionType exceptionType;

    public RecommendException(RecommendExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}