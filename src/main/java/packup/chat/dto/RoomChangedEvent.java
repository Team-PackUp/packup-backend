package packup.chat.dto;


import java.util.List;
import java.util.Map;

public record RoomChangedEvent(
        Long chatRoomSeq, List<Long> targetUserIds, Map<Long, ChatRoomResponse> userSpecificDTO
) {}
