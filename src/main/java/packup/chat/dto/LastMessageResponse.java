package packup.chat.dto;

import java.time.LocalDateTime;

public interface LastMessageResponse {
    Long getSeq();
    String getMessage();
    String getFileFlag();
    LocalDateTime getCreatedAt();
}
