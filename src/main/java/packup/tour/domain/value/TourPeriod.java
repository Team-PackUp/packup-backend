package packup.tour.domain.value;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <pre>
 * TourPeriod (관광 기간 값 객체)
 *
 * 투어의 실제 진행 기간(시작 일시 ~ 종료 일시)을 표현하는 값 객체.
 * 관광 시작 일시와 종료 일시의 무결성을 보장하며,
 * 투어 등록 또는 일정 변경 시 기간 오류를 사전에 방지.
 * </pre>
 *
 * <p><b>주요 제약:</b> 시작 일시는 종료 일시보다 이전이어야 함</p>
 *
 * <p><b>예시:</b>
 * <pre>{@code
 * TourPeriod period = TourPeriod.builder()
 *     .startDateTime(LocalDateTime.of(2025, 5, 10, 9, 0))
 *     .endDateTime(LocalDateTime.of(2025, 5, 10, 18, 0))
 *     .build();
 * }</pre>
 *
 * @author SBLEE
 * @since 2025.05.12
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourPeriod {

    /**
     * 관광 시작 일시
     */
    private LocalDateTime startDateTime;

    /**
     * 관광 종료 일시
     */
    private LocalDateTime endDateTime;

    /**
     * 생성자 (Builder 사용)
     *
     * @param startDateTime 관광 시작 일시
     * @param endDateTime 관광 종료 일시
     * @throws IllegalArgumentException 시작 일시가 종료 일시보다 이후일 경우
     */
    @Builder
    public TourPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validatePeriod(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * 관광 기간의 유효성을 검증.
     *
     * @param startDateTime 관광 시작 일시
     * @param endDateTime 관광 종료 일시
     */
    private void validatePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("관광 시작 일시는 종료 일시보다 이후일 수 없습니다.");
        }
    }
}
