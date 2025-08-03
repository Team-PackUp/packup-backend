package packup.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;

@Getter
@NoArgsConstructor
public class UserDetailRequest {
    private String userGender;
    private String userAge;
    private String userNation;
    private YnType marketingFlag;
    private YnType pushFLag;

}