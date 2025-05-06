package packup.payment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.payment.domain.PaymentAmount;

public interface PaymentAmountRepository extends JpaRepository<PaymentAmount, Long> {
}
