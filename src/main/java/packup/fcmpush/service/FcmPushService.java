package packup.fcmpush.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.fcmpush.domain.UserFcmToken;
import packup.fcmpush.domain.repository.UserFcmTokenRepository;
import packup.fcmpush.dto.FcmPushRequest;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FcmPushService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserFcmTokenRepository userFcmTokenRepository;

    public void sendBackground(FcmPushRequest firebaseRequest) throws FirebaseMessagingException {

        List<UserFcmToken> userFcmTokenList = userFcmTokenRepository.findAllByUserSeqIn(firebaseRequest.getUserList());

        for (UserFcmToken userList : userFcmTokenList) {

            Notification notification = Notification.builder()
                    .setTitle(firebaseRequest.getTitle())
                    .setBody(firebaseRequest.getBody())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification) // notification > 백그라운드/앱종료 상태 알림 전송
                    .setToken("cYKDNc5uSqKvBoiXr9QSLB:APA91bFd4LgAMu-pPKF7C1fDB9kboEAouzJTE_SEZuMRxDCX5g-GBh7SMXqPhJ65vCdIB-aVgnQZkPnuvvEIA7kRwoo6xbkMoEa54dBGDXRb2tZTG0qZ-9Y")
                    .build();

            firebaseMessaging.send(message);
        }
    }
//
//    @Transactional
//    public void registerOrUpdateFcmToken(Long memberId, String token) {
//        Optional<UserFcmToken> existing = userFcmTokenRepository.findByFcmToken(token);
//
//        if (existing.isPresent()) {
//            UserFcmToken tokenEntity = existing.get();
//
//            if (!tokenEntity.getUserSeq().getSeq().equals(user.getSeq())) {
//                throw new IllegalStateException("다른 유저의 FCM 토큰입니다.");
//            }
//
//            // 같은 유저면 활성화 갱신
//            tokenEntity.setActiveFlag(YnType.Y);
//            tokenEntity.setUpdatedAt(LocalDateTime.now());
//        } else {
//            // 새 토큰 저장
//            UserFcmToken newToken = UserFcmToken.builder()
//                    .userSeq(user)
//                    .fcmToken(token)
//                    .activeFlag(YnType.Y)
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            fcmTokenRepository.save(newToken);
//        }
//    }
//
//    @Transactional
//    public void deactivateFcmToken(String token) {
//        userFcmTokenRepository.findByFcmToken(token)
//                .ifPresent(t -> t.setActiveFlag(YnType.N));
//    }
}
