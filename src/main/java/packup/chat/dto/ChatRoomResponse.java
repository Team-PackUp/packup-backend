package packup.chat.dto;

import lombok.*;
import packup.common.enums.YnType;
import packup.user.dto.UserInfoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    private Long seq;
    private List<Long> partUserSeq;
    private UserInfoResponse user;
    private String title;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
    private int unReadCount;
    private YnType fileFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
