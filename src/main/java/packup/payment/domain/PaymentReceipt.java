package packup.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "payment_receipt")
public class PaymentReceipt extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_seq", nullable = false)
    private PaymentHistory payment;

    @Lob
    private String receiptUrl;

    public void assignPayment(PaymentHistory payment) {
        this.payment = payment;
    }
}
