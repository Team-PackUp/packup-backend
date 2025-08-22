package packup.guide.domain;

import lombok.Getter;

@Getter
public enum GuideApplicationStatus {
    APPLIED ("060001", "APPLIED"),
    APPROVED("060002", "APPROVED"),
    REJECTED("060003", "REJECTED"),
    CANCELED("060004", "CANCELED"),
    DRAFTED("060005", "DRAFTED");

    private final String code;
    private final String name;

    GuideApplicationStatus(String code, String name) {
        this.code = code; this.name = name;
    }

    public static GuideApplicationStatus from(Integer codeInt) {
        if (codeInt == null) return null;
        final String code = String.format("%06d", codeInt);
        for (var s : values()) if (s.code.equals(code)) return s;
        return null;
    }
}
