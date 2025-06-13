package packup.fcmpush.enums;

public enum DeepLinkType {
    CHAT("chat_room");

    private final String value;

    DeepLinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
