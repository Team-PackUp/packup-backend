package packup.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chatRoomSeq;
    private Long userSeq;
    private String message;
    private boolean fileFlag;
}
