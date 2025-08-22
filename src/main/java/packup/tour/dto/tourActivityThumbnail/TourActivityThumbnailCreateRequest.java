package packup.tour.dto.tourActivityThumbnail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourActivity;
import packup.tour.domain.TourActivityThumbnail;

/**
 * TourActivityThumbnail 생성 요청 DTO
 * - createdAt은 Auditing으로 처리
 * - seq는 자동 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourActivityThumbnailCreateRequest {

    /** 투어 체험 식별번호 (FK) */
    @NotNull(message = "tourActivitySeq는 필수입니다.")
    private Long tourActivitySeq;

    /** 섬네일 순서 (1 이상) */
    @NotNull(message = "thumbnailOrder는 필수입니다.")
    @Min(value = 1, message = "thumbnailOrder는 1 이상이어야 합니다.")
    private Integer thumbnailOrder;

    /** 섬네일 이미지 URL (필수) */
    @NotBlank(message = "thumbnailImageUrl은 필수입니다.")
    private String thumbnailImageUrl;

    /**
     * 서비스/유즈케이스에서 조회해 온 TourActivity 엔티티를 이용해
     * 새 TourActivityThumbnail 엔티티를 생성합니다.
     */
    public TourActivityThumbnail toEntity(TourActivity tourActivity) {
        return TourActivityThumbnail.builder()
                .tourActivity(tourActivity)
                .thumbnailOrder(this.thumbnailOrder)
                .thumbnailImageUrl(this.thumbnailImageUrl)
                .build();
    }
}
