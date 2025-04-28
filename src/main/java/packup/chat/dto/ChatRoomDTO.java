package packup.chat.dto;

import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private Long seq;
    private List<Long> partUserSeq;
    private Long userSeq;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
