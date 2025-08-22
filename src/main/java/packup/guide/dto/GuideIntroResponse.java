package packup.guide.dto;

import packup.guide.domain.GuideInfo;

public record GuideIntroResponse(
        Integer years,
        String roleSummary,
        String expertise,
        String achievement,
        String summary
) {
    public static GuideIntroResponse of(GuideInfo gi) {
        return new GuideIntroResponse(
                (int) gi.getYears(),
                gi.getRoleSummary(),
                gi.getExpertise(),
                gi.getAchievement(),
                gi.getSummary()
        );
    }
}
