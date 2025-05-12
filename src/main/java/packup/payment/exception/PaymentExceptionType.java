package packup.payment.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum PaymentExceptionType implements BaseExceptionType {

    TOSS_CONFIRM_FAILED(BAD_REQUEST, "Toss 결제 승인에 실패했습니다."),
    DUPLICATE_ORDER_ID(CONFLICT, "이미 결제가 완료된 주문입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
