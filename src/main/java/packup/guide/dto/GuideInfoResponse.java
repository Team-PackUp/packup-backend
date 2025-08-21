package packup.guide.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import packup.guide.domain.GuideInfo;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GuideInfoResponse {

    private Long seq;
    private Long userSeq;

    private String guideName;

    private String telNumber;
    private String telNumber2;

    private JsonNode languages;

    private String guideIntroduce;

    private short guideRating;
    private String guideAvatarPath;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GuideInfoResponse from(GuideInfo guideInfo) {
        return GuideInfoResponse.builder()
                .seq(guideInfo.getSeq())
                .userSeq(guideInfo.getUser().getSeq())
//                .guideName(guideInfo.getGuideName())
//                .telNumber(guideInfo.getTelNumber())
//                .telNumber2(guideInfo.getTelNumber2())
//                .languages(guideInfo.getLanguages())
                .guideIntroduce(guideInfo.getGuideIntroduce())
//                .guideRating(guideInfo.getGuideRating())
//                .guideAvatarPath(guideInfo.getGuideAvatarPath())
                .createdAt(guideInfo.getCreatedAt())
                .updatedAt(guideInfo.getUpdatedAt())
                .build();
    }
}
