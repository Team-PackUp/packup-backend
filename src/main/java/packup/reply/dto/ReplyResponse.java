package packup.reply.dto;

import lombok.*;
import packup.reply.domain.Reply;
import packup.reply.enums.TargetType;
import packup.user.domain.UserInfo;
import packup.user.dto.UserInfoResponse;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ReplyResponse {
    private long seq;
    private long targetSeq;
    private UserInfoResponse user;
    private String targetType;
    private String content;
    private int point;
    private LocalDateTime createdAt;

    public static ReplyResponse fromEntity(Reply reply) {
        return ReplyResponse.builder()
                .seq(reply.getSeq())
                .user(UserInfoResponse.of(reply.getUser()))
                .targetSeq(reply.getTargetSeq())
                .targetType(reply.getTargetType())
                .content(reply.getContent())
                .point(reply.getPoint())
                .createdAt(reply.getCreatedAt())
                .build();
    }
}
