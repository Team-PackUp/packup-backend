package packup.tour.domain.value;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <pre>
 * RecruitmentPeriod (모집 기간 값 객체)
 *
 * 투어 모집 기간(startDate ~ endDate)을 하나의 값 객체로 묶어 관리.
 * 도메인 내에서 기간 무결성(시작일 ≤ 종료일)을 생성자 수준에서 강제하며,
 * 투어 등록 및 수정 시 비즈니스 로직의 일관성을 보장.
 * </pre>
 *
 * <p><b>주요 검증:</b>
 * 시작일이 종료일보다 늦을 경우 IllegalArgumentException 발생</p>
 *
 * <p><b>예시:</b>
 * <pre>{@code
 * RecruitmentPeriod period = RecruitmentPeriod.builder()
 *     .startDate(LocalDate.of(2025, 5, 1))
 *     .endDate(LocalDate.of(2025, 5, 10))
 *     .build();
 * }</pre>
 *
 * @author SBLEE
 * @since 2025.05.12
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentPeriod {

    /**
     * 모집 시작일
     */
    private LocalDate startDate;

    /**
     * 모집 종료일
     */
    private LocalDate endDate;

    /**
     * 모집 기간 생성자
     *
     * @param startDate 모집 시작일
     * @param endDate 모집 종료일
     * @throws IllegalArgumentException 시작일이 종료일보다 늦을 경우 발생
     */
    @Builder
    public RecruitmentPeriod(LocalDate startDate, LocalDate endDate) {
        validatePeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * 모집 기간 무결성 검증
     * 시작일이 종료일보다 이후일 경우 예외를 발생시킴
     *
     * @param startDate 모집 시작일
     * @param endDate 모집 종료일
     */
    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("모집 시작일은 종료일보다 이후일 수 없습니다.");
        }
    }
}
