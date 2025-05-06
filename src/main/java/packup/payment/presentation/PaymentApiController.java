package packup.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.common.dto.ResultModel;
import packup.payment.dto.PaymentConfirmRequest;
import packup.payment.dto.PaymentConfirmResponse;
import packup.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResultModel<PaymentConfirmResponse> confirmPayment(@RequestBody PaymentConfirmRequest request) {
        return null;
    }

}
