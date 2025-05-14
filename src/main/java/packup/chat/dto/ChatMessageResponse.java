package packup.chat.dto;

import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    private Long seq;
    private Long chatRoomSeq;
    private Long userSeq;
    private String message;
    private LocalDateTime createdAt;
    private boolean fileFlag;
}
