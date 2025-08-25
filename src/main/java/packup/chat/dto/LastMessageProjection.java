package packup.chat.dto;

import java.time.LocalDateTime;

public interface LastMessageProjection {
    Long getSeq();
    String getMessage();
    String getFileFlag();
    LocalDateTime getCreatedAt();
}
