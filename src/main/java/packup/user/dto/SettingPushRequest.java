package packup.user.dto;

import lombok.Getter;
import lombok.Setter;
import packup.common.enums.YnType;

@Setter
@Getter
public class SettingPushRequest {

    private YnType pushFlag;
    private YnType marketingFlag;

}

