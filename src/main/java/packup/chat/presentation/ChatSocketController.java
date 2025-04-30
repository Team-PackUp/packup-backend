package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import packup.chat.config.RedisPublisher;
import packup.chat.dto.ChatMessageDTO;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessageDTO chatMessage) {
        System.out.println("STOMP 연결 " + chatMessage.getMessage());
        
        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomSeq, content);
        redisPublisher.publishMessage("chatRoom:" + chatRoomSeq, content);
    }
}
