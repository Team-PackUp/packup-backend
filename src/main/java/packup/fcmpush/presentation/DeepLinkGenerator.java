package packup.fcmpush.presentation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import packup.fcmpush.enums.DeepLinkType;

@Component
public class DeepLinkGenerator {

    public static String generate(DeepLinkType type, Long parameter1, String parameter2) {
        return switch (type) {
            case CHAT_MESSAGE -> String.format("/chat_message/%d/%s", parameter1, encodePathSegment(parameter2));
        };
    }

    // 공백 처리
    private static String encodePathSegment(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8)
                .replace(" ", "%20");
    }
}
