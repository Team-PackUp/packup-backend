package packup.tour.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TourSessionStatusCode {

    OPEN(110001, "투어 세션 모집중"),
    FULL(110002, "정원 초과"),
    COMPLETED(110003, "투어 종료"),
    CANCELED(110004, "취소됨");

    private final int code;
    private final String description;

    public static TourSessionStatusCode fromCode(int code) {
        for (TourSessionStatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown TourSessionStatus code: " + code);
    }
}
