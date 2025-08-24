package packup.fcmpush.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import packup.user.dto.UserPushTarget;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FcmPushRequest {
    private List<String> tokenList;
    private String title;
    private String body;
    private DeepLink deepLink;
}
