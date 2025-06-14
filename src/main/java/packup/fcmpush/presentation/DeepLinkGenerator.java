package packup.fcmpush.presentation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import packup.fcmpush.dto.DeepLink;
import packup.fcmpush.enums.DeepLinkType;

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
