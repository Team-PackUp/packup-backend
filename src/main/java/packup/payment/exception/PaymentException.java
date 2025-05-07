package packup.payment.exception;

import lombok.RequiredArgsConstructor;
import packup.auth.exception.AuthExceptionType;
import packup.common.exception.BaseException;
import packup.common.exception.BaseExceptionType;

@RequiredArgsConstructor
public class PaymentException extends BaseException {

    private final PaymentExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}