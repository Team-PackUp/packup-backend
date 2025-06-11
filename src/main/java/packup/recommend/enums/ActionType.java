package packup.recommend.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActionType {
    CLICK("CLICK"),
    LIKE("LIKE"),
    TOUR("TOUR");

    private final String s;

    public String getString() {
        return s;
    }

}
