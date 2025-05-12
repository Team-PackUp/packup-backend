package packup.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "payment_history")
public class PaymentHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(name = "payment_key", nullable = false, unique = true)
    private String paymentKey;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "status")
    private String status;

    @Column(name = "method")
    private String method;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // 연관 관계
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentAmount amount;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentMethodInfo methodInfo;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentReceipt receipt;

    public void assignAmount(PaymentAmount amount) {
        this.amount = amount;
        amount.assignPayment(this);
    }

    public void assignMethodInfo(PaymentMethodInfo methodInfo) {
        this.methodInfo = methodInfo;
        methodInfo.assignPayment(this);
    }

    public void assignReceipt(PaymentReceipt receipt) {
        this.receipt = receipt;
        receipt.assignPayment(this);
    }
}
