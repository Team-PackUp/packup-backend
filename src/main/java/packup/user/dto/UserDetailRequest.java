package packup.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDetailRequest {
    private String userGender;
    private String userAge;
    private String userNation;

}