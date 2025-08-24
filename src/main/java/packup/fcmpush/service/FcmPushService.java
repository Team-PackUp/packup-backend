package packup.fcmpush.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.enums.YnType;
import packup.common.util.JsonUtil;
import packup.fcmpush.domain.UserFcmToken;
import packup.fcmpush.domain.repository.UserFcmTokenRepository;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.exception.FcmPushException;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.exception.UserException;

import java.util.*;
import java.util.stream.Collectors;

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

    // 마케팅 수신 동의 여부, 알림 푸시 동의 여부, 회원 탈퇴 여부 확인 후 발송
    @Transactional
    public void requestFcmPush(FcmPushRequest req) throws FirebaseMessagingException {

        List<String> tokens = req.getTokenList() != null
                ? req.getTokenList().stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList())
                : Collections.emptyList();
        if (tokens.isEmpty()) return;

        Map<String, String> data = new HashMap<>();
        data.put("title", req.getTitle());
        data.put("body", req.getBody());
        if (req.getDeepLink() != null) {
            data.put("deepLink", JsonUtil.toJson(req.getDeepLink()));
        }

        MulticastMessage mm = MulticastMessage.builder()
                .putAllData(data)
                .addAllTokens(tokens)
                .build();

        BatchResponse resp = firebaseMessaging.sendEachForMulticast(mm);

        List<SendResponse> results = resp.getResponses();
        for (int i = 0; i < results.size(); i++) {
            SendResponse r = results.get(i);
            if (!r.isSuccessful()) {
                FirebaseMessagingException ex = r.getException();
                if (ex != null) {
                    if (ex.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                        String bad = tokens.get(i);
                        System.err.println("Fail token: " + bad);
                        // userFcmTokenRepository.deactivateByToken(bad);
                    }
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
                existing.updateUser(user);
            }

            existing.activate();
//            existing.setUpdatedAt(LocalDateTime.now());
            existing.updateOsType(osTypeCode);
        }, () -> {
            UserFcmToken newToken = UserFcmToken.of(
                            user, token, osTypeCode, YnType.Y
                    );
            userFcmTokenRepository.save(newToken);
        });
    }

    @Transactional
    public void deactivateFcmToken(String token) {
        userFcmTokenRepository.findByFcmToken(token)
                .ifPresent(UserFcmToken::deactivate);
    }
}
