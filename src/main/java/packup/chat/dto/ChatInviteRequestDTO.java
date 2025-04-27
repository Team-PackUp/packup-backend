package packup.chat.dto;

import lombok.Getter;

@Getter
public class ChatInviteRequestDTO {
    private Long chatRoomSeq;
    private Long newPartUserSeq;
}
