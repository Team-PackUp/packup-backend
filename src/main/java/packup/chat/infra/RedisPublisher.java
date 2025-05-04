package packup.chat.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import packup.chat.dto.ChatMessageDTO;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final StringRedisTemplate redisTemplate;

    private final ChannelTopic sendMessageTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishMessage(ChatMessageDTO chatMessageDTO) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(chatMessageDTO);
        redisTemplate.convertAndSend(sendMessageTopic.getTopic(), json);
    }

    // 새로운 채팅방 메시지 발행
    public void publishChatRoomList() {
        System.out.println("채팅방 최신화");
        String updateMessage = "{\"update\": \"chatRoomList\"}";

        redisTemplate.convertAndSend("chat_rooms", updateMessage);
    }
}
