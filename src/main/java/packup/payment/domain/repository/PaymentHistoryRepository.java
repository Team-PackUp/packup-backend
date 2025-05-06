package packup.payment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.payment.domain.PaymentHistory;

import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    Optional<PaymentHistory> findByOrderId(String orderId);
    Optional<PaymentHistory> findByPaymentKey(String paymentKey);
    boolean existsByOrderId(String orderId);
}
