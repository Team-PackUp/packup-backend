package packup.fcmpush.presentation;

import org.springframework.stereotype.Component;
import packup.fcmpush.dto.DeepLink;
import packup.fcmpush.enums.DeepLinkType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class DeepLinkGenerator {

    public static DeepLink generate(DeepLinkType type, Map<String, Object> parameter) {
        return DeepLink.builder()
                .deepLinkType(type)
                .parameter(parameter)
                .build();
    }


    // 공백 처리
    private static String encodePathSegment(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8)
                .replace(" ", "%20");
    }
}
