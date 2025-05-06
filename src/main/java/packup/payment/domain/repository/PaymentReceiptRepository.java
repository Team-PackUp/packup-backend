package packup.payment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.payment.domain.PaymentReceipt;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
}
