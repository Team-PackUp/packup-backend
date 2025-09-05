package packup.tour.dto.tourSession;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourSession;
import packup.tour.enums.TourSessionStatusCode;

import java.time.LocalDateTime;

/**
 * TourSession 업데이트 요청 DTO
 * - 연관관계(tour)는 변경하지 않습니다.
 * - 엔티티의 update(start, end, status[Enum])와 1:1 매핑됩니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourSessionUpdateRequest {

    /** 세션 시작 시간 */
    @NotNull(message = "sessionStartTime은 필수입니다.")
    private LocalDateTime sessionStartTime;

    /** 세션 종료 시간 */
    @NotNull(message = "sessionEndTime은 필수입니다.")
    private LocalDateTime sessionEndTime;

    /** 세션 상태 코드(int) → Enum으로 변환하여 적용 */
    @NotNull(message = "sessionStatusCode는 필수입니다.")
    private Integer sessionStatusCode;

    /** 시작 < 종료 유효성 */
    @AssertTrue(message = "sessionEndTime은 sessionStartTime 이후여야 합니다.")
    public boolean isValidTimeRange() {
        if (sessionStartTime == null || sessionEndTime == null) return true; // 각 필드는 @NotNull로 검증
        return sessionEndTime.isAfter(sessionStartTime);
    }

    /** 엔티티에 변경사항 적용 (Integer → Enum 변환) */
    public void applyTo(TourSession entity) {
        final TourSessionStatusCode status = TourSessionStatusCode.fromCode(this.sessionStatusCode);
        entity.update(this.sessionStartTime, this.sessionEndTime, status);
    }
}
