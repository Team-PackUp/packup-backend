package packup.tour.enums;

import lombok.Getter;

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

    /**
     * 임시저장 상태 (작성 중이지만 공개되지 않음)
     */
    TEMP("임시저장"),

    /**
     * 모집중 상태 (사용자에게 노출됨)
     */
    RECRUITING("모집중"),

    /**
     * 모집완료 상태 (최소 인원 충족)
     */
    RECRUITED("모집완료"),

    /**
     * 출발 대기 상태 (모집 완료 후 출발 전)
     */
    READY("출발대기"),

    /**
     * 투어 진행 중
     */
    ONGOING("투어중"),

    /**
     * 투어 종료 상태
     */
    FINISHED("종료");

    private final String label;

    TourStatusCode(String label) {
        this.label = label;
    }
}
