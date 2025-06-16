package packup.reply.dto;

import lombok.*;
import packup.reply.enums.TargetType;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ReplyRequest {
    private Long targetSeq;
    private TargetType targetType;
    private String content;
}
