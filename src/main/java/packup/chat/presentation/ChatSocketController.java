package packup.chat.presentation;

import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.chat.infra.RedisPublisher;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.service.ChatService;
import packup.config.security.provider.JwtTokenProvider;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisPublisher redisPublisher;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/send.message")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Message<?> stompMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(stompMessage);
        String token = (String) accessor.getSessionAttributes().get("Authorization");
        Long userSeq = Long.valueOf(jwtTokenProvider.getUsername(token));

        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .message(content)
                .chatRoomSeq(chatRoomSeq)
                .userSeq(userSeq)
                .build();

        ChatMessageDTO saveResult = chatService.saveChatMessage(chatMessageDTO);

        if (saveResult.getSeq() > 0) {
//            redisPublisher.publishMessage("chatRoom:" + chatRoomSeq, String.valueOf(saveResult));

            messagingTemplate.convertAndSend("/topic/chat/room/" + chatRoomSeq, saveResult);
        }
    }


    @MessageMapping("/send.connection")
    public void sendCheckSocket(String connection) {
        System.out.println("STOMP 연결 유지 " + connection);
    }
}
