package packup.chat.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import packup.chat.constant.ChatConstant;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    // Redis에서 pub 했을 때 Sub로 메시지를 수신했을 때 > 즉각 리턴하여 UX 경험 굿
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody());
        try {
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});

            String chatRoomSeq = map.get("chatRoomSeq").toString();

            messagingTemplate.convertAndSend(ChatConstant.CHAT_ENDPOINT_PREFIX + chatRoomSeq, map);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}


