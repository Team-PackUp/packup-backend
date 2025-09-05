package packup.tour.exception;

import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

public class TourSessionException extends BaseException {

    private final TourSessionExceptionType exceptionType;

    public TourSessionException(TourSessionExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
