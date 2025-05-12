package packup.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.warn("JSON 파싱 실패: {}", json, e);
            return null;
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.warn("JSON 파싱 실패: {}", json, e);
            return null;
        }
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("JSON 직렬화 실패: {}", object, e);
            return null;
        }
    }
}
