package packup.tour.dto.tourListing;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TourCreateRequest {

    // jsonb 배열
    @NotNull
    @Size(min = 1, message = "키워드는 최소 1개 이상이어야 합니다.")
    private List<@NotBlank String> tourKeywords;

    @NotBlank
    @Size(max = 255)
    private String tourTitle;

    @NotBlank
    private String tourIntroduce;

    // TEXT로 요약(줄바꿈 허용)
    private String tourIncludedContent;
    private String tourExcludedContent;

    @Size(max = 255)
    private String tourNotes;

    private Integer minHeadCount;
    private Integer maxHeadCount;

    private Integer tourLocationCode;

    private String tourThumbnailUrl;

    @NotNull
    @Positive
    private Long tourPrice;

    private String meetUpAddress;

    // 위경도는 null 허용, 제약은 DB check 제약으로 검증 + 서비스단 추가 검증
    private Double meetUpLat;
    private Double meetUpLng;

    // 'Y' or 'N'
    @Pattern(regexp = "Y|N")
    private String transportServiceFlag;

    @Pattern(regexp = "Y|N")
    private String privateFlag;

    private Long privatePrice;

    @Pattern(regexp = "Y|N")
    private String adultContentFlag;

    // 예: "100001"(TEMP). 미전달 시 서비스에서 기본값으로 세팅 가능
    private String tourStatusCode;

    private String memo;

    // 하위 목록들
    @Valid
    private List<ActivityCreate> activities;

    // (선택) 전체 사진 URL 목록
    private List<String> photos;

    // ---- nested DTOs ----
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActivityCreate {
        @NotNull
        @Positive
        private Integer activityOrder;

        @NotBlank
        @Size(max = 255)
        private String activityTitle;

        @Size(max = 255)
        private String activityIntroduce;

        @Positive
        private Integer activityDurationMinute;

        @Valid
        private List<ActivityThumbCreate> thumbnails;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActivityThumbCreate {
        @NotNull
        @Positive
        private Integer thumbnailOrder;

        @NotBlank
        private String thumbnailImageUrl;
    }
}
