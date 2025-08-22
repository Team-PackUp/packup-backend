package packup.tour.dto.tourActivity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.tour.domain.TourActivity;

/**
 * TourActivity 업데이트 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourActivityUpdateRequest {

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

    /** 삭제 여부 (Y/N) */
    @NotNull(message = "deletedFlag는 필수입니다.")
    private YnType deletedFlag;

    /** 엔티티에 변경사항 적용 */
    public void applyTo(TourActivity entity) {
        entity.update(
                this.activityOrder,
                this.activityTitle,
                this.activityIntroduce,
                this.activityDurationMinute,
                this.deletedFlag
        );
    }
}
