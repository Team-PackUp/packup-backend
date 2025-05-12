package packup.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PaymentConfirmResponse {
    private String paymentKey;
    private String type;
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private String method;
    private Long totalAmount;
    private Long balanceAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private boolean useEscrow;
    private String lastTransactionKey;
    private Long suppliedAmount;
    private int vat;
    private boolean cultureExpense;
    private Long taxFreeAmount;
    private Long taxExemptionAmount;

    private List<Cancel> cancels;

    private boolean isPartialCancelable;
    private Card card;
    private VirtualAccount virtualAccount;
    private String secret;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private Transfer transfer;
    private Receipt receipt;
    private Checkout checkout;
    private EasyPay easyPay;
    private String country;
    private Failure failure;
    private CashReceipt cashReceipt;
    private List<CashReceiptHistory> cashReceipts;
    private Discount discount;
    private Map<String, String> metadata;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Discount {
        private Integer amount;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class CashReceipt {
        private String type;
        private String receiptKey;
        private String issueNumber;
        private String receiptUrl;
        private Long amount;
        private Long taxFreeAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CashReceiptHistory {
        private String receiptKey;
        private String orderId;
        private String orderName;
        private String type;
        private String issueNumber;
        private String receiptUrl;
        private String businessNumber;
        private String transactionType;
        private Integer amount;
        private Integer taxFreeAmount;
        private String issueStatus;
        private Failure failure;
        private String customerIdentityNumber;
        private String requestedAt;
    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class Failure {
        private String code;
        private String message;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class EasyPay {
        private String provider;
        private Long amount;
        private Long discountAmount;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Checkout {
        private String url;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Receipt {
        private String url;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Transfer {
        private String bankCode;
        private String settlementStatus;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Card {
        private Long amount;
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanMonths;
        private String approveNo;
        private boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private boolean isInterestFree;
        private String interestPayer;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GiftCertificate {
        private String approveNo;
        private String settlementStatus;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class MobilePhone {
        private String customerMobilePhone;
        private String settlementStatus;
        private String receiptUrl;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class VirtualAccount {
        private String accountType;
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
        private String refundStatus;
        private boolean expired;
        private String settlementStatus;
        private RefundReceiveAccount refundReceiveAccount;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class RefundReceiveAccount {
            private String bankCode;
            private String accountNumber;
            private String holderName;
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Cancel {
        private Integer cancelAmount;
        private String cancelReason;
        private Integer taxFreeAmount;
        private Integer taxExemptionAmount;
        private Integer refundableAmount;
        private Integer transferDiscountAmount;
        private Integer easyPayDiscountAmount;
        private String canceledAt;
        private String transactionKey;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String receiptKey;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String cancelRequestId;

        private String cancelStatus;
    }
}
