package packup.fcmpush.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import packup.fcmpush.enums.DeepLinkType;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class DeepLink {
    private DeepLinkType deepLinkType;
    private List<Object> parameter;
}