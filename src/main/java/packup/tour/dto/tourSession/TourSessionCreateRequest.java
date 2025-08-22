package packup.tour.dto.tourSession;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourInfo;
import packup.tour.domain.TourSession;

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

    /** 투어 정보 식별번호 (FK) */
    @NotNull(message = "tourSeq는 필수입니다.")
    private Long tourSeq;

    /** 세션 시작/종료 시간 */
    @NotNull(message = "sessionStartTime은 필수입니다.")
    private LocalDateTime sessionStartTime;

    @NotNull(message = "sessionEndTime은 필수입니다.")
    private LocalDateTime sessionEndTime;

    /** 세션 상태 코드(선택) */
    private Integer sessionStatusCode;

    /** 도메인 규칙: 종료가 시작 이후여야 함 */
    public boolean isValidTimeRange() {
        return sessionStartTime == null || sessionEndTime == null || sessionEndTime.isAfter(sessionStartTime);
    }

    /**
     * 사전 조회한 TourInfo 엔티티로 신규 TourSession 엔티티 생성
     */
    public TourSession toEntity(TourInfo tour) {
        TourSession.TourSessionBuilder builder = TourSession.builder()
                .tour(tour)
                .sessionStartTime(sessionStartTime)
                .sessionEndTime(sessionEndTime);

        if (sessionStatusCode != null) {
            builder.sessionStatusCode(sessionStatusCode);
        }

        return builder.build();
    }
}
