package packup.payment.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import packup.payment.domain.*;
import packup.payment.dto.PaymentConfirmResponse;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final ObjectMapper objectMapper;

    public PaymentHistory toPaymentHistory(UserInfo user, PaymentConfirmResponse res) {
        PaymentHistory history = PaymentHistory.builder()
                .user(user)
                .orderId(res.getOrderId())
                .paymentKey(res.getPaymentKey())
                .orderName(res.getOrderName())
                .status(res.getStatus())
                .method(res.getMethod())
                .requestedAt(parse(res.getRequestedAt()))
                .approvedAt(parse(res.getApprovedAt()))
                .build();

        PaymentAmount amount = PaymentAmount.builder()
                .totalAmount(res.getTotalAmount())
                .balanceAmount(res.getBalanceAmount())
                .suppliedAmount(res.getSuppliedAmount())
                .vat(res.getVat())
                .taxFreeAmount(res.getTaxFreeAmount())
                .taxExemptionAmount(res.getTaxExemptionAmount())
                .build();

        history.assignAmount(amount);

        if (res.getCard() != null || res.getEasyPay() != null) {
            PaymentMethodInfo methodInfo = PaymentMethodInfo.builder()
                    .cardNumber(res.getCard() != null ? res.getCard().getNumber() : null)
                    .cardType(res.getCard() != null ? res.getCard().getCardType() : null)
                    .issuerCode(res.getCard() != null ? res.getCard().getIssuerCode() : null)
                    .approveNo(res.getCard() != null ? res.getCard().getApproveNo() : null)
                    .easyPayProvider(res.getEasyPay() != null ? res.getEasyPay().getProvider() : null)
                    .isInterestFree(res.getCard() != null ? res.getCard().isInterestFree() : null)
                    .installmentMonths(res.getCard() != null ? res.getCard().getInstallmentPlanMonths() : null)
                    .build();
            history.assignMethodInfo(methodInfo);
        }

        if (res.getReceipt() != null) {
            PaymentReceipt receipt = PaymentReceipt.builder()
                    .receiptUrl(res.getReceipt().getUrl())
                    .build();
            history.assignReceipt(receipt);
        }

        return history;
    }

    private LocalDateTime parse(String iso) {
        return iso != null ? OffsetDateTime.parse(iso).toLocalDateTime() : null;
    }
}
