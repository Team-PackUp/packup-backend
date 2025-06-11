package packup.chat.dto;

import lombok.Getter;

@Getter
public class ReadMessageRequest {
    private Long chatRoomSeq;
    private Long lastReadMessageSeq;
}
