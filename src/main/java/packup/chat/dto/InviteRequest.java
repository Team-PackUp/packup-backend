package packup.chat.dto;

import lombok.Getter;

@Getter
public class InviteRequest {
    private Long chatRoomSeq;
    private Long newPartUserSeq;
}
