package packup.chat.dto;

import lombok.*;

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
    private Long userSeq;
    private String nickNames;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
    private int unReadCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
