package packup.payment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import packup.payment.domain.PaymentMethodInfo;

public interface PaymentMethodInfoRepository extends JpaRepository<PaymentMethodInfo, Long> {
}
