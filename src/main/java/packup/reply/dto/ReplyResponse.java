package packup.reply.dto;

import lombok.*;
import packup.reply.domain.Reply;
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

    public static ReplyResponse fromEntity(Reply reply) {
        return ReplyResponse.builder()
                .seq(reply.getSeq())
                .targetSeq(reply.getTargetSeq())
                .targetType(reply.getTargetType())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .build();
    }
}
