package packup.fcmpush.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.enums.YnType;
import packup.fcmpush.domain.UserFcmToken;
import packup.fcmpush.domain.repository.UserFcmTokenRepository;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.exception.FcmPushException;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.exception.UserException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static packup.fcmpush.exception.FcmPushExceptionType.INVALID_TOKEN_OWNER;
import static packup.user.exception.UserExceptionType.NOT_FOUND_MEMBER;


@Service
@RequiredArgsConstructor
public class FcmPushService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final UserInfoRepository userInfoRepository;

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

    @Transactional
    public void registerOrUpdateFcmToken(Long memberId, String token, String osType) {
        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(NOT_FOUND_MEMBER));

        userFcmTokenRepository.findByFcmToken(token).ifPresentOrElse(existing -> {
            boolean isOwner = existing.getUserSeq().getSeq().equals(memberId);

            if (!isOwner) {
                if (existing.getActiveFlag() == YnType.Y) {
                    throw new FcmPushException(INVALID_TOKEN_OWNER);
                }
                existing.setUserSeq(user);
            }

            existing.setActiveFlag(YnType.Y);
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setOsType(osType);
        }, () -> {
            UserFcmToken newToken = UserFcmToken.builder()
                    .userSeq(user)
                    .fcmToken(token)
                    .activeFlag(YnType.Y)
                    .updatedAt(LocalDateTime.now())
                    .osType(osType)
                    .build();
            userFcmTokenRepository.save(newToken);
        });
    }

    @Transactional
    public void deactivateFcmToken(String token) {
        userFcmTokenRepository.findByFcmToken(token)
                .ifPresent(t -> t.setActiveFlag(YnType.N));
    }
}
