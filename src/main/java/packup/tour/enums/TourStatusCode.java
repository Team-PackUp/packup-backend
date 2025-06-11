package packup.tour.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * <pre>
 * TourStatus (투어 상태 코드 열거형)
 *
 * 투어의 상태 흐름을 표현하는 Enum.
 * 프론트엔드 및 관리자 페이지 등에서 상태명을 사용자 친화적으로 제공하기 위해 label을 함께 제공.
 *
 * 상태 흐름 예시:
 * - TEMP → RECRUITING → RECRUITED → READY → ONGOING → FINISHED
 * </pre>
 *
 * <p><b>사용 예:</b>
 * <pre>{@code
 * TourStatus status = TourStatus.RECRUITING;
 * String label = status.getLabel(); // "모집중"
 * }</pre>
 *
 * @author SBLEE
 * @since 2025.05.12
 */
@Getter
public enum TourStatusCode {

    TEMP("100001", "임시저장"),
    RECRUITING("100002", "모집중"),
    RECRUITED("100003", "모집완료"),
    READY("100004", "출발대기"),
    ONGOING("100005", "투어중"),
    FINISHED("100006", "종료");

    private final String code;
    private final String label;

    TourStatusCode(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static TourStatusCode fromCode(String code) {
        return Arrays.stream(values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TourStatusCode code: " + code));
    }
}
