package packup.chat.presentation;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import packup.chat.domain.ChatRead;
import packup.chat.dto.ChatMessageRequest;
import packup.chat.dto.ChatMessageResponse;
import packup.chat.dto.ChatRoomResponse;
import packup.chat.dto.ReadMessageRequest;
import packup.chat.service.ChatService;
import packup.config.security.provider.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatSocketMapper {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/send.message")
    public void sendMessage(@Payload ChatMessageRequest chatMessage, Message<?> stompMessage) {

        Long userSeq = getUserSeqInSocket(stompMessage);
        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        // 메시지 DTO 생성
        ChatMessageRequest chatMessageDTO = ChatMessageRequest.builder()
                .message(content)
                .chatRoomSeq(chatRoomSeq)
                .userSeq(userSeq)
                .fileFlag(chatMessage.getFileFlag())
                .build();

        // 채팅 저장
        ChatMessageResponse newChatMessageDTO = chatService.saveChatMessage(userSeq, chatMessageDTO);

        if (newChatMessageDTO.getSeq() > 0) {
            List<Long> targetFcmUserSeq = new ArrayList<>();

            messagingTemplate.convertAndSend("/topic/chat/room/" + chatRoomSeq, newChatMessageDTO);

            List<Long> chatRoomPartUser = chatService.getPartUserInRoom(chatRoomSeq);

            // 회원별로 따로 구독 response
            for (Long username : chatRoomPartUser) {
                ChatRoomResponse userSpecificDTO = chatService.getChatRoom(username, chatRoomSeq);

                if (!userSeq.equals(username)) {
                    targetFcmUserSeq.add(username);
                }

                messagingTemplate.convertAndSendToUser(
                        username.toString(), "/queue/chatroom-refresh", userSpecificDTO
                );
            }

            // FCM
            chatService.chatSendFcmPush(newChatMessageDTO, targetFcmUserSeq);
        }
    }


    @MessageMapping("/read.message")
    public void readChatMessage(@Payload ReadMessageRequest readMessageRequest, Message<?> stompMessage) {

        Long userSeq = getUserSeqInSocket(stompMessage);

        chatService.readChatMessage(userSeq, readMessageRequest);
    }



    @MessageMapping("/send.connection")
    public void sendKeepSocket(String connection) {
        System.out.println("STOMP 연결 유지 " + connection);
    }

    private Long getUserSeqInSocket(Message<?> stompMessage) {
        // 헤더에서 Authorization 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(stompMessage);
        String token = (String) accessor.getSessionAttributes().get("Authorization");

        // JWT 토큰에서 username (userSeq) 추출
        return Long.valueOf(jwtTokenProvider.getUsername(token));
    }
}
