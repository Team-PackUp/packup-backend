package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.service.ChatService;
import packup.config.security.provider.JwtTokenProvider;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatSocketMapping {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/send.message")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Message<?> stompMessage) {

        // 헤더에서 Authorization 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(stompMessage);
        String token = (String) accessor.getSessionAttributes().get("Authorization");

        // JWT 토큰에서 username (userSeq) 추출
        Long userSeq = Long.valueOf(jwtTokenProvider.getUsername(token));

        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        // 새로운 채팅 메시지 DTO 생성
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .message(content)
                .chatRoomSeq(chatRoomSeq)
                .userSeq(userSeq)
                .fileFlag(chatMessage.isFileFlag())
                .build();

        // 채팅 저장
        ChatMessageDTO newChatMessageDTO = chatService.saveChatMessage(userSeq, chatMessageDTO);

        // 구독 알림
        if (newChatMessageDTO.getSeq() > 0) {
            System.out.println("소켓 결과 전송!!");
            messagingTemplate.convertAndSend("/topic/chat/room/" + chatRoomSeq, newChatMessageDTO);

            List<Long> chatRoomPartUser = chatService.getPartUserInRoom(chatRoomSeq);
            for (Long username : chatRoomPartUser) {
                messagingTemplate.convertAndSendToUser(username.toString(), "/queue/chatroom-refresh", "REFRESH");
            }
        }
    }



    @MessageMapping("/send.connection")
    public void sendCheckSocket(String connection) {
        System.out.println("STOMP 연결 유지 " + connection);
    }
}
