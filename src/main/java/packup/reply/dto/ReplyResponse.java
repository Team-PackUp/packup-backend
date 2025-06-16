package packup.reply.dto;

import lombok.*;
import packup.reply.enums.TargetType;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ReplyResponse {
    private long seq;
    private long targetSeq;
    private String targetType;
    private String content;
    private LocalDateTime createdAt;
}
