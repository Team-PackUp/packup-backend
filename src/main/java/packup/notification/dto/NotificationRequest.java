package packup.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import packup.user.domain.UserInfo;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class NotificationRequest {
    private List<UserInfo> userList;
    private String title;
    private String body;

}
