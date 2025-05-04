package packup.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import packup.chat.constant.ChatConstant;
import packup.chat.dto.ChatMessageDTO;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void appendToStream(ChatMessageDTO jsonMessage) {
        try {
            String json = objectMapper.writeValueAsString(jsonMessage);
            Map<String, String> content = Map.of("message", json);

            MapRecord<String, String, String> record =
                    MapRecord.create(ChatConstant.CHAT_STREAM_KEY, content);

            redisTemplate.opsForStream().add(record);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
