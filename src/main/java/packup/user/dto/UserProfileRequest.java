package packup.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import packup.common.enums.YnType;

import java.util.List;

@Builder
@Setter
@Getter
public class UserProfileRequest {
    private String profileImagePath;
    private String nickName;
    private String language;
    private List<String> preference;
}

