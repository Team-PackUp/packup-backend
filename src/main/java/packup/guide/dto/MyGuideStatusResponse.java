package packup.guide.dto;

import lombok.Builder;

@Builder
public record MyGuideStatusResponse(
        boolean isGuide,
        Application application
) {
    @Builder
    public record Application(
            boolean exists,
            String statusCode,
            String statusName,
            String rejectReason,
            boolean canReapply
    ) {}
}
