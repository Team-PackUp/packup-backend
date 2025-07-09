package packup.alert.enums;

public enum AlertType {

    RESERVATION_COMPLETE("050001", "RESERVATION_COMPLETE"),
    RESERVATION_MODIFIED("050002", "RESERVATION_MODIFIED"),
    RESERVATION_CANCEL  ("050003", "RESERVATION_CANCEL");

    private final String code;   // DB 저장값
    private final String text;   // 사용자 표시용

    AlertType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String code() { return code; }
    public String text() { return text; }

    public static AlertType fromCode(String code) {
        for (AlertType t : values()) {
            if (t.code.equals(code)) return t;
        }
        throw new IllegalArgumentException("unknown alert code: " + code);
    }
}
