package packup.chat.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.service.ChatService;

@Component
@RequiredArgsConstructor
public class RedisStreamListener implements StreamListener<String, MapRecord<String, String, String>> {
    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void onMessage(MapRecord<String, String, String> message) {

        try {
            String json = message.getValue().get("message");

            ChatMessageDTO dto = objectMapper.readValue(json, ChatMessageDTO.class);

            chatService.saveChatMessage(dto);
            chatService.updateChatRoom(dto.getChatRoomSeq());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
