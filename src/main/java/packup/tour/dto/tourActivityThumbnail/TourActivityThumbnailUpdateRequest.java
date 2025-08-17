package packup.tour.dto.tourActivityThumbnail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourActivityThumbnail;

/**
 * TourActivityThumbnail 업데이트 요청 DTO
 * - 연관관계(tourActivity)는 변경하지 않습니다.
 * - 엔티티의 update(thumbnailOrder, thumbnailImageUrl)와 1:1 매핑.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourActivityThumbnailUpdateRequest {

    /** 섬네일 순서 (1 이상) */
    @NotNull(message = "thumbnailOrder는 필수입니다.")
    @Min(value = 1, message = "thumbnailOrder는 1 이상이어야 합니다.")
    private Integer thumbnailOrder;

    /** 섬네일 이미지 URL (필수) */
    @NotBlank(message = "thumbnailImageUrl은 필수입니다.")
    private String thumbnailImageUrl;

    /** 엔티티에 변경사항 적용 */
    public void applyTo(TourActivityThumbnail entity) {
        entity.update(this.thumbnailOrder, this.thumbnailImageUrl);
    }
}
