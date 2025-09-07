package packup.tour.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class TourInfoException extends BaseException {

    private final TourInfoExceptionType exceptionType;

    public TourInfoException(TourInfoExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
