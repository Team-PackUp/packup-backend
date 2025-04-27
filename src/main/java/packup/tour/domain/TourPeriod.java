package packup.tour.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 관광 기간(TourPeriod) 값 객체
 * - 관광 시작 일시와 종료 일시를 관리
 * - 기간 무결성을 검증
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourPeriod {

    private LocalDateTime startDateTime;  // 관광 시작 일시
    private LocalDateTime endDateTime;    // 관광 종료 일시

    @Builder
    public TourPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validatePeriod(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * 관광 시작 일시와 종료 일시의 유효성을 검증
     * @param startDateTime 관광 시작 일시
     * @param endDateTime 관광 종료 일시
     */
    private void validatePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("관광 시작 일시는 종료 일시보다 이후일 수 없습니다.");
        }
    }
}
