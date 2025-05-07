package packup.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "payment_method_info")
public class PaymentMethodInfo extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_seq", nullable = false)
    private PaymentHistory payment;

    private String cardNumber;
    private String cardType;
    private String issuerCode;
    private String approveNo;
    private String easyPayProvider;
    private Boolean isInterestFree;
    private Integer installmentMonths;

    public void assignPayment(PaymentHistory payment) {
        this.payment = payment;
    }
}
