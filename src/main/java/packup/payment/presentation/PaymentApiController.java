package packup.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
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
    public ResultModel<PaymentConfirmResponse> confirmPayment(@Auth Long memberId, @RequestBody PaymentConfirmRequest request) {
        PaymentConfirmResponse paymentConfirmResponse = paymentService.confirm(memberId, request);
        return ResultModel.success(paymentConfirmResponse);
    }

}
