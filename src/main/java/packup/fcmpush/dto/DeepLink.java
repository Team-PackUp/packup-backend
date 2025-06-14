package packup.fcmpush.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import packup.fcmpush.enums.DeepLinkType;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class DeepLink {
    private DeepLinkType deepLinkType;
    private Map<String, Object> parameter;
}