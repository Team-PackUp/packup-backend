package packup.reply.dto;

import lombok.*;
import packup.fcmpush.dto.FcmPushRequest;
import packup.reply.enums.TargetType;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ReplyRequest {
    private Long seq;
    private Long targetSeq;
    private TargetType targetType;
    private String content;
    private Integer point;
    private FcmPushRequest fcmPushRequest;
}
