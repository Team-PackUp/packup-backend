package packup.fcmpush.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.enums.YnType;
import packup.fcmpush.domain.UserFcmToken;
import packup.fcmpush.domain.repository.UserFcmTokenRepository;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.exception.FcmPushException;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.exception.UserException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static packup.fcmpush.exception.FcmPushExceptionType.INVALID_OS_TYPE;
import static packup.fcmpush.exception.FcmPushExceptionType.INVALID_TOKEN_OWNER;
import static packup.user.exception.UserExceptionType.NOT_FOUND_MEMBER;


@Service
@RequiredArgsConstructor
public class FcmPushService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public void requestFcmPush(FcmPushRequest firebaseRequest) {


        List<UserFcmToken> userFcmTokenList = userFcmTokenRepository.findAllByUserSeqInAndActiveFlag(firebaseRequest.getUserSeqList(), YnType.Y);

        for (UserFcmToken userList : userFcmTokenList) {

            Map<String, String> data = new HashMap<>();
            data.put("title", firebaseRequest.getTitle());
            data.put("body", firebaseRequest.getBody());

            Optional.ofNullable(firebaseRequest.getDeepLink())
                    .filter(s -> !s.isEmpty())
                    .ifPresent(deepLink -> data.put("deepLink", deepLink));

            Message message = Message.builder()
                    .putAllData(data)
                    .setToken(userList.getFcmToken())
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                    System.err.println("Fail token: " + userList.getFcmToken());
                }
            }
        }
    }

    @Transactional
    public void registerOrUpdateFcmToken(Long memberId, String token, String osType) {
        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(NOT_FOUND_MEMBER));

        String osTypeCode = commonCodeRepository.findByCodeName(osType.toUpperCase())
                        .orElseThrow(() -> new FcmPushException(INVALID_OS_TYPE))
                        .getCodeId();

        userFcmTokenRepository.findByFcmToken(token).ifPresentOrElse(existing -> {
            boolean isOwner = existing.getUser().getSeq().equals(memberId);

            if (!isOwner) {
                if (existing.getActiveFlag() == YnType.Y) {
                    throw new FcmPushException(INVALID_TOKEN_OWNER);
                }
                existing.setUser(user);
            }

            existing.setActiveFlag(YnType.Y);
//            existing.setUpdatedAt(LocalDateTime.now());
            existing.setOsType(osTypeCode);
        }, () -> {
            UserFcmToken newToken = UserFcmToken.builder()
                    .user(user)
                    .fcmToken(token)
                    .activeFlag(YnType.Y)
//                    .updatedAt(LocalDateTime.now())
                    .osType(osTypeCode)
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
