package packup.guide.dto;

public record GuideMeResponse(
        boolean isGuide,
        GuideInfoResponse guide // 가이드가 아니면 null
) {
    public static GuideMeResponse none() { return new GuideMeResponse(false, null); }
    public static GuideMeResponse of(GuideInfoResponse guide) { return new GuideMeResponse(true, guide); }
}