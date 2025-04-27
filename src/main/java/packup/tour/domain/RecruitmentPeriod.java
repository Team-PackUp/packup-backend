package packup.tour.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 모집 기간(RecruitmentPeriod) 값 객체dd
 * - 모집 시작일과 종료일을 관리
 * - 기간 무결성을 검증
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentPeriod {

    private LocalDate startDate;  // 모집 시작일
    private LocalDate endDate;    // 모집 종료일

    @Builder
    public RecruitmentPeriod(LocalDate startDate, LocalDate endDate) {
        validatePeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * 모집 시작일과 종료일의 유효성을 검증한다.
     * @param startDate 모집 시작일
     * @param endDate 모집 종료일
     */
    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("모집 시작일은 종료일보다 이후일 수 없습니다.");
        }
    }
}
