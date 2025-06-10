package packup.chat.dto;

import lombok.*;
import packup.common.enums.YnType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chatRoomSeq;
    private Long userSeq;
    private String message;
    private YnType fileFlag;
    private String deepLink;
}
