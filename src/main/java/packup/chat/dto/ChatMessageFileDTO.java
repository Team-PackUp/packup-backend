package packup.chat.dto;

import lombok.*;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageFileDTO {
    private Long seq;
    private String chatFilePath;
    private UserInfo userSeq;
    private String encodedName;
    private String realName;
    private LocalDateTime createdAt;
}
