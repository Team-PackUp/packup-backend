package packup.tour.dto.tourActivity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourActivity;
import packup.tour.domain.TourInfo;

/**
 * TourActivity 생성 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourActivityCreateRequest {

    /** 투어 정보 식별번호 (FK) */
    @NotNull(message = "tourSeq는 필수입니다.")
    private Long tourSeq;

    /** 체험 순서 (1 이상) */
    @NotNull(message = "activityOrder는 필수입니다.")
    @Min(value = 1, message = "activityOrder는 1 이상이어야 합니다.")
    private Integer activityOrder;

    /** 체험 제목 (필수, 255자 이하) */
    @NotBlank(message = "activityTitle은 필수입니다.")
    @Size(max = 255, message = "activityTitle은 255자 이하여야 합니다.")
    private String activityTitle;

    /** 체험 소개 (선택, 255자 이하) */
    @Size(max = 255, message = "activityIntroduce는 255자 이하여야 합니다.")
    private String activityIntroduce;

    /** 체험 소요시간(분) (선택, 1 이상일 때만 유효) */
    @Positive(message = "activityDurationMinute가 존재한다면 1 이상이어야 합니다.")
    private Integer activityDurationMinute;

    /**
     * 서비스/유즈케이스에서 조회한 TourInfo 엔티티를 전달해
     * 신규 TourActivity 엔티티로 변환합니다.
     *
     * @param tour 사전 조회된 TourInfo 엔티티 (필수)
     */
    public TourActivity toEntity(TourInfo tour) {
        return TourActivity.builder()
                .tour(tour)
                .activityOrder(this.activityOrder)
                .activityTitle(this.activityTitle)
                .activityIntroduce(this.activityIntroduce)
                .activityDurationMinute(this.activityDurationMinute)
                .build();
    }
}
