package packup.chat.dto;


import java.util.List;

public record RoomChangedEvent(Long chatRoomSeq, List<Long> targetUserIds) {}
