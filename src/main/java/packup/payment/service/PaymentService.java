package packup.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.payment.domain.PaymentHistory;
import packup.payment.domain.repository.PaymentHistoryRepository;
import packup.payment.dto.PaymentConfirmRequest;
import packup.payment.dto.PaymentConfirmResponse;
import packup.payment.infra.auth.TossAuthorizationProvider;
import packup.payment.infra.client.TossPaymentClient;
import packup.payment.mapper.PaymentMapper;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TossPaymentClient tossPaymentClient;
    private final TossAuthorizationProvider tossAuthorizationProvider;

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentMapper paymentMapper;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public PaymentConfirmResponse confirm(Long memberId, PaymentConfirmRequest request) {
        PaymentConfirmResponse res =  tossPaymentClient.confirmPayment(request, tossAuthorizationProvider.getAuthHeader());

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        PaymentHistory history = paymentMapper.toPaymentHistory(userInfo, res);
        paymentHistoryRepository.save(history);

        return res;
    }

}
