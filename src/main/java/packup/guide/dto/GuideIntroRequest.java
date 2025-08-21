package packup.guide.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record GuideIntroRequest(
        @Min(0) @Max(50) Integer years,
        @Size(max = 90) String roleSummary,
        @Size(max = 90) String expertise,
        @Size(max = 90) String achievement,
        @Size(max = 90) String summary
) {}

