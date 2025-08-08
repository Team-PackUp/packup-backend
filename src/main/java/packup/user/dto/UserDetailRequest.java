package packup.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserDetailRequest {
    private String userGender;
    private LocalDate userBirth;
    private String userNation;
    private String userLanguage;
    private YnType marketingFlag;
    private YnType pushFLag;

}