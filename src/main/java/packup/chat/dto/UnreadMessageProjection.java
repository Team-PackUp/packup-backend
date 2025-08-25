package packup.chat.dto;

public interface UnreadMessageProjection {
    Long getUserSeq();
    Integer getUnread();
}
