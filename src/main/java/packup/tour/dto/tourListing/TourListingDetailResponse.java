package packup.tour.dto.tourListing;

import java.util.List;

/**
 * 투어 상세 조회/수정 응답 DTO
 * 프런트의 loadForEdit()에서 기대하는 키와 1:1 매칭
 */
public record TourListingDetailResponse(
        Long seq,
        List<String> tourKeywords,
        String tourTitle,
        String tourIntroduce,
        String tourIncludedContent,
        String tourExcludedContent,
        String tourNotes,
        Long tourLocationCode,
        String tourThumbnailUrl,
        Long tourPrice,
        Integer minHeadCount,
        Integer maxHeadCount,
        String meetUpAddress,
        Double meetUpLat,
        Double meetUpLng,
        String transportServiceFlag,  // 'Y'/'N'
        String privateFlag,           // 'Y'/'N'
        Long privatePrice,
        String adultContentFlag,      // 'Y'/'N'
        String memo,
        List<Activity> activities,
        List<String> photos
) {
    public static record Activity(
            Integer activityOrder,
            String activityTitle,
            String activityIntroduce,
            Integer activityDurationMinute,
            List<String> thumbnailUrls
    ) {}
}
