package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.service.ChatSocketService;
import packup.config.security.provider.JwtTokenProvider;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatSocketService chatSocketService;
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
                .build();

        // 서비스로 메시지 전송
        chatSocketService.sendChatMessage(chatMessageDTO);
    }



    @MessageMapping("/send.connection")
    public void sendCheckSocket(String connection) {
        System.out.println("STOMP 연결 유지 " + connection);
    }
}
