package packup.tour.dto.tourActivityThumbnail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourActivityThumbnail;

import java.time.LocalDateTime;

/**
 * TourActivityThumbnail 응답 DTO
 * - 연관관계는 ID(tourActivitySeq)만 노출
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourActivityThumbnailResponse {

    /** 투어 체험 섬네일 식별번호 (PK) */
    private Long seq;

    /** 섬네일 순서 */
    private Integer thumbnailOrder;

    /** 섬네일 이미지 URL */
    private String thumbnailImageUrl;

    /** 등록일시 */
    private LocalDateTime createdAt;

    /** 엔티티 → DTO 매핑 */
    public static TourActivityThumbnailResponse from(TourActivityThumbnail entity) {
        return TourActivityThumbnailResponse.builder()
                .seq(entity.getSeq())
                .thumbnailOrder(entity.getThumbnailOrder())
                .thumbnailImageUrl(entity.getThumbnailImageUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
