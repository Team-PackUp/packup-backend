package packup.reply.enums;

public enum TargetType {
    TOUR("관광"),
    GUIDE("가이드");

    private final String value;

    TargetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
