package packup.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileImageResponse {
    private Long seq;
    private String profileImagePath;
}

