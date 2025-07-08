package packup.alert.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertType {
    RESERVATION_COMPLETE("RESERVATION_COMPLETE"),
    RESERVATION_MODIFIED("RESERVATION_MODIFIED"),
    RESERVATION_CANCEL("RESERVATION_CANCEL");

    private final String s;

    public String getString() {
        return s;
    }

}
