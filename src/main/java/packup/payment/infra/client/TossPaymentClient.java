package packup.payment.infra.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import packup.payment.dto.PaymentConfirmRequest;
import packup.payment.dto.PaymentConfirmResponse;

public interface TossPaymentClient {

    @PostExchange(url = "https://api.tosspayments.com/v1/payments/confirm")
    PaymentConfirmResponse confirmPayment(
            @RequestBody PaymentConfirmRequest request,
            @RequestHeader("Authorization") String authorization
    );
}
