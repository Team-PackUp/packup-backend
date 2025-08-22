package packup.tour.dto.tourReview;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourReview;

/**
 * TourReview 업데이트 요청 DTO
 * - 연관관계(tour, tourSession, user)는 변경하지 않습니다.
 * - 엔티티의 update(rating, comment)와 1:1 매핑됩니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourReviewUpdateRequest {

    /** 후기 점수 (정책에 맞게 범위 설정) */
    @NotNull(message = "reviewRating은 필수입니다.")
    @Min(value = 0, message = "reviewRating은 0 이상이어야 합니다.")
    @Max(value = 5, message = "reviewRating은 5 이하여야 합니다.")
    private Integer reviewRating;

    /** 후기 코멘트 */
    @NotBlank(message = "reviewComment는 필수입니다.")
    private String reviewComment;

    /** 엔티티에 변경사항 적용 */
    public void applyTo(TourReview entity) {
        entity.update(this.reviewRating, this.reviewComment);
    }
}
