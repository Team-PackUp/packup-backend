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
    private long userSeq;
    private String nickName;
    private String targetType;
    private String content;
    private String profileImagePath;
    private LocalDateTime createdAt;

    public static ReplyResponse fromEntity(Reply reply) {
        return ReplyResponse.builder()
                .seq(reply.getSeq())
                .userSeq(reply.getUser().getSeq())
                .nickName(reply.getUser().getDetailInfo().getNickname())
                .targetSeq(reply.getTargetSeq())
                .targetType(reply.getTargetType())
                .content(reply.getContent())
                .profileImagePath(reply.getUser().getDetailInfo().getProfileImagePath())
                .createdAt(reply.getCreatedAt())
                .build();
    }
}
