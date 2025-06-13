package packup.fcmpush.presentation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;
import packup.fcmpush.dto.DeepLink;
import packup.fcmpush.enums.DeepLinkType;

@Component
public class DeepLinkGenerator {

    public static DeepLink generate(DeepLinkType type, Long parameter1) {
        return DeepLink.builder()
                .deepLinkType(type)
                .parameter(List.of(parameter1))
                .build();
    }

    public static DeepLink generate(DeepLinkType type, Long parameter1, String parameter2) {
        return DeepLink.builder()
                .deepLinkType(type)
                .parameter(List.of(parameter1, parameter2))
                .build();
    }


    // 공백 처리
    private static String encodePathSegment(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8)
                .replace(" ", "%20");
    }
}
