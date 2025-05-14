package packup.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import packup.firebase.dto.FirebaseRequest;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserDetailInfoRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserDetailInfoRepository userDetailInfoRepository;
    private final List<String> tokenList = new ArrayList<>();

    FcmService(FirebaseMessaging firebaseMessaging, UserDetailInfoRepository userDetailInfoRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.userDetailInfoRepository = userDetailInfoRepository;
    }

    public void sendBackground(FirebaseRequest firebaseRequest) throws FirebaseMessagingException{

        for (UserInfo s : firebaseRequest.getUserList()) {
            userDetailInfoRepository.findByUser(s)
                    .map(UserDetailInfo::getFcmToken)
                    .ifPresent(tokenList::add);
        }


        for (String token : tokenList) {

            Notification notification = Notification.builder()
                    .setTitle(firebaseRequest.getTitle())
                    .setBody(firebaseRequest.getBody())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(token)
                    .build();

            firebaseMessaging.send(message);
        }
    }
}
