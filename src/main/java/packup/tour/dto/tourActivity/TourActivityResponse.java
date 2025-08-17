package packup.tour.dto.tourActivity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.tour.domain.TourActivity;
import packup.tour.dto.tourInfo.TourInfoResponse;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourActivityResponse {

    /** 투어 체험 식별번호 (PK) */
    private Long seq;

    /** 투어 정보 식별번호 (FK) */
    private TourInfoResponse tour;

    /** 체험 순서 */
    private Integer activityOrder;

    /** 체험 제목 */
    private String activityTitle;

    /** 체험 소개 */
    private String activityIntroduce;

    /** 체험 소요시간 (분) */
    private Integer activityDurationMinute;

    /** 삭제 여부 (Y/N) */
    private YnType deletedFlag;

    /** 등록일시 */
    private LocalDateTime createdAt;

    /** 수정일시 */
    private LocalDateTime updatedAt;

    /** 엔티티 → DTO 매핑 */
    public static TourActivityResponse from(TourActivity tourActivity) {
        return TourActivityResponse.builder()
                .seq(tourActivity.getSeq())
                .tour(TourInfoResponse.from(tourActivity.getTour()))
                .activityOrder(tourActivity.getActivityOrder())
                .activityTitle(tourActivity.getActivityTitle())
                .activityIntroduce(tourActivity.getActivityIntroduce())
                .activityDurationMinute(tourActivity.getActivityDurationMinute())
                .deletedFlag(tourActivity.getDeletedFlag())
                .createdAt(tourActivity.getCreatedAt())
                .updatedAt(tourActivity.getUpdatedAt())
                .build();
    }
}
