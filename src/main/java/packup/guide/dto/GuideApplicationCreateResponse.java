package packup.guide.dto;

public record GuideApplicationCreateResponse(
        Long seq,
        String guideIdcardImageUrl,
        String guideIntroduce
) {}
