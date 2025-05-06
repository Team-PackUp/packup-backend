package packup.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "payment_amount")
public class PaymentAmount extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentHistory payment;

    private Long totalAmount;
    private Long balanceAmount;
    private Long suppliedAmount;
    private Integer vat;
    private Long taxFreeAmount;
    private Long taxExemptionAmount;

    public void assignPayment(PaymentHistory payment) {
        this.payment = payment;
    }
}
