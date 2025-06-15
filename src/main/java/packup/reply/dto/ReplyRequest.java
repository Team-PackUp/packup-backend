package packup.reply.dto;

import lombok.*;
import packup.reply.enums.TargetType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequest {
    private long targetSeq;
    private TargetType targetType;
    private String content;
}
