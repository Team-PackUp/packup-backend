package packup.tour.dto.tourSession;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourInfo;
import packup.tour.domain.TourSession;
import packup.tour.enums.TourSessionStatusCode;

import java.time.LocalDateTime;

/**
 * TourSession 생성 요청 DTO
 * - deletedFlag: 엔티티 기본값(N) 사용
 * - createdAt / updatedAt: Auditing 처리
 * - cancelledAt: 생성 시 미설정
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourSessionCreateRequest {

    @NotNull(message = "tourSeq는 필수입니다.")
    private Long tourSeq;

    @NotNull(message = "sessionStartTime은 필수입니다.")
    private LocalDateTime sessionStartTime;

    @NotNull(message = "sessionEndTime은 필수입니다.")
    private LocalDateTime sessionEndTime;

    private Integer sessionStatusCode;

    @NotNull(message = "maxParticipants는 필수입니다.")
    @Min(value = 1, message = "maxParticipants는 최소 1 이상이어야 합니다.")
    private Integer maxParticipants;

    @AssertTrue(message = "sessionEndTime은 sessionStartTime 이후여야 합니다.")
    public boolean isValidTimeRange() {
        if (sessionStartTime == null || sessionEndTime == null) return true; // 필수 필드는 @NotNull에서 검증
        return sessionEndTime.isAfter(sessionStartTime);
    }

    public TourSession toEntity(TourInfo tour) {
        final TourSessionStatusCode status =
                (sessionStatusTimeNotNull() && sessionStatusCode != null)
                        ? TourSessionStatusCode.fromCode(sessionStatusCode)
                        : TourSessionStatusCode.OPEN;

        return TourSession.of(
                tour,
                sessionStartTime,
                sessionEndTime,
                status,
                maxParticipants
        );
    }

    private boolean sessionStatusTimeNotNull() {
        return sessionStartTime != null && sessionEndTime != null;
    }
}
