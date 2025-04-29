package packup.chat.presentation;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import packup.chat.dto.ChatMessageDTO;

@Controller
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessageDTO chatMessage) {
        System.out.println("STOMP 연결");
        
        Long chatRoomSeq = chatMessage.getChatRoomSeq();
        String content = chatMessage.getMessage();

        // 채팅방별 구독 topic으로 publish
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomSeq, content);
    }
}
