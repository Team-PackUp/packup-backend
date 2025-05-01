package packup.chat.presentation;

import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
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
    public void sendMessage(@Auth Long memberId, ChatMessageDTO chatMessage) {
//        String token = authHeader.replace("Bearer ", "");
//        System.out.println("Token: " + token);
//        System.out.println("STOMP 연결 " + chatMessage.getMessage());
        System.out.println("채팅 발송 회원 " + memberId);

//        Long userSeq = Long.valueOf(jwtTokenProvider.getUsername(token));
        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .message(content)
                .chatRoomSeq(chatRoomSeq)
//                .userSeq(userSeq)
                .build();
        boolean saveResult = chatService.saveChatMessage(chatMessageDTO);
        if (saveResult) {
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomSeq, content);
            redisPublisher.publishMessage("chatRoom:" + chatRoomSeq, content);
        }
    }

    @MessageMapping("/send.connection")
    public void sendCheckSocket(String connection) {
        System.out.println("STOMP 연결 유지 " + connection);
    }
}
