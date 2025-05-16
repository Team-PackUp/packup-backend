package packup.chat.presentation;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import packup.chat.dto.ChatMessageResponse;
import packup.chat.dto.ChatRoomResponse;
import packup.chat.service.ChatService;
import packup.config.security.provider.JwtTokenProvider;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.service.FcmPushService;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatSocketMapping {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoRepository userInfoRepository;
    private final FcmPushService firebaseService;

    private final List<Long> targetFcmUserSeq = new ArrayList<>();

    @MessageMapping("/send.message")
    public void sendMessage(@Payload ChatMessageResponse chatMessage, Message<?> stompMessage) throws FirebaseMessagingException {

        // 헤더에서 Authorization 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(stompMessage);
        String token = (String) accessor.getSessionAttributes().get("Authorization");

        // JWT 토큰에서 username (userSeq) 추출
        Long userSeq = Long.valueOf(jwtTokenProvider.getUsername(token));

        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        // 새로운 채팅 메시지 DTO 생성
        ChatMessageResponse chatMessageDTO = ChatMessageResponse.builder()
                .message(content)
                .chatRoomSeq(chatRoomSeq)
                .userSeq(userSeq)
                .fileFlag(chatMessage.isFileFlag())
                .build();

        // 채팅 저장
        ChatMessageResponse newChatMessageDTO = chatService.saveChatMessage(userSeq, chatMessageDTO);
        ChatRoomResponse firstChatRoomDTO = chatService.getChatRoom(newChatMessageDTO.getChatRoomSeq());

        // STOMP 구독 알림
        if (newChatMessageDTO.getSeq() > 0) {
            messagingTemplate.convertAndSend("/topic/chat/room/" + chatRoomSeq, newChatMessageDTO);

            List<Long> chatRoomPartUser = chatService.getPartUserInRoom(chatRoomSeq);
            for (Long username : chatRoomPartUser) {
                targetFcmUserSeq.add(username);
                messagingTemplate.convertAndSendToUser(username.toString(), "/queue/chatroom-refresh", firstChatRoomDTO);
            }
        }

        // FCM 알림
        List<UserInfo> targetFcmUserList = userInfoRepository.findAllBySeqIn(targetFcmUserSeq);
        if(targetFcmUserList.size() > 0) {
            FcmPushRequest firebaseRequest = FcmPushRequest
                    .builder()
                    .userList(targetFcmUserList)
                    .title("메시지가 도착했습니다.")
                    .body(newChatMessageDTO.getMessage())
                    .build();

            firebaseService.sendBackground(firebaseRequest);
        }
    }



    @MessageMapping("/send.connection")
    public void sendKeepSocket(String connection) {
        System.out.println("STOMP 연결 유지 " + connection);
    }
}
