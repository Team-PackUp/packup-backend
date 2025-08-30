package packup.tour.dto.tourListing;

import java.time.LocalDate;
import packup.tour.domain.TourInfo;

public record TourListingResponse(
        Long id,
        String titleKo,
        String titleEn,
        String categoryNameKo,   // "체험"(Experience) 로 고정해서 사용.. 임시
        String categoryNameEn,
        String coverImagePath,
        String statusCode,
        LocalDate startDate
) {
    public static TourListingResponse from(TourInfo t) {
        return new TourListingResponse(
                t.getSeq(),
                t.getTourTitle(),
                null,
                "체험",
                "Experience",
                t.getTourThumbnailUrl(),
                t.getTourStatusCode() != null ? t.getTourStatusCode().name() : null,
                t.getCreatedAt() != null ? t.getCreatedAt().toLocalDate() : null
        );
    }
}
