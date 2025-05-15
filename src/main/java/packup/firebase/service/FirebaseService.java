package packup.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.firebase.domain.UserFcmToken;
import packup.firebase.domain.repository.UserFcmTokenRepository;
import packup.firebase.dto.FirebaseRequest;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserFcmTokenRepository userFcmTokenRepository;

    public void sendBackground(FirebaseRequest firebaseRequest) throws FirebaseMessagingException {

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
}
