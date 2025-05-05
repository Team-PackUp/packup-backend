package packup.chat.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.service.ChatService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final StringRedisTemplate redisTemplate;

    private final ChannelTopic sendMessageTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;

    public void publishMessage(ChatMessageDTO chatMessageDTO) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(chatMessageDTO);
        redisTemplate.convertAndSend(sendMessageTopic.getTopic(), json);
    }

    // 새로운 채팅방 메시지 발행
    public void publishChatRoomList(ChatMessageDTO chatMessageDTO) throws JsonProcessingException {

//        List<Long> chatRoomPartUser = chatService.getPartUserInRoom(chatMessageDTO.getChatRoomSeq());

        System.out.println("채팅방 최신화");
        String newChat = chatMessageDTO.getMessage();
        Long chatRoomSeq = chatMessageDTO.getSeq();

        System.out.println("채팅방 최신화2 " + newChat);
        System.out.println("채팅방 최신화3 " + chatRoomSeq);

        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("chatRoomSeq", chatRoomSeq);
        chatMap.put("chatMessage", newChat);

        String newChatInfo = objectMapper.writeValueAsString(chatMap);


        redisTemplate.convertAndSend("chat_rooms", newChatInfo);
    }
}
