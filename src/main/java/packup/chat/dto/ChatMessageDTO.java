package packup.chat.dto;

import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Long seq;
    private Long chatRoomSeq;
    private Long userSeq;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
