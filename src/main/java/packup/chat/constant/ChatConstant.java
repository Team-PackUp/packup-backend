package packup.chat.constant;

public class ChatConstant {
    public static final String CHAT_STREAM_KEY = "packup_message";
    public static final String CHAT_GROUP_NAME = "message_group";
    public static final String REDIS_TOPIC_NAME = "message_channel";
    public static final String CHAT_ENDPOINT_PREFIX = "/topic/chat/room/";
    public static final String CHAT_ROOM_REFRESH = "/queue/chatroom-refresh";
    public static final int PAGE_SIZE = 10;
    public static final String REPLACE_IMAGE_TEXT = "사진";
    public static final int UNREAD_CHAT_COUNT_LIMIT = 10;
}
