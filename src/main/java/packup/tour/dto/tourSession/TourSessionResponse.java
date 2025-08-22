package packup.tour.dto.tourSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.tour.domain.TourSession;
import packup.tour.dto.tourInfo.TourInfoResponse;

import java.time.LocalDateTime;

/**
 * TourSession 응답 DTO
 * - 연관관계는 ID(tourSeq)만 노출
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourSessionResponse {

    /** 투어 세션 식별번호 (PK) */
    private Long seq;

    /** 투어 정보 식별번호 (FK) */
    private TourInfoResponse tour;

    /** 세션 시작/종료 시간 */
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;

    /** 세션 상태 코드 */
    private Integer sessionStatusCode;

    /** 세션 중단 시간 */
    private LocalDateTime cancelledAt;

    /** 삭제 여부 (Y/N) */
    private YnType deletedFlag;

    /** 등록/수정 일시 */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 엔티티 → DTO 매핑 */
    public static TourSessionResponse from(TourSession tourSession) {
        return TourSessionResponse.builder()
                .seq(tourSession.getSeq())
                .tour(TourInfoResponse.from(tourSession.getTour()))
                .sessionStartTime(tourSession.getSessionStartTime())
                .sessionEndTime(tourSession.getSessionEndTime())
                .sessionStatusCode(tourSession.getSessionStatusCode())
                .cancelledAt(tourSession.getCancelledAt())
                .deletedFlag(tourSession.getDeletedFlag())
                .createdAt(tourSession.getCreatedAt())
                .updatedAt(tourSession.getUpdatedAt())
                .build();
    }
}
