package packup.guide.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import packup.guide.domain.GuideApplication;
import packup.guide.domain.GuideApplicationStatus;
import packup.guide.domain.repository.GuideApplicationRepository;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.guide.dto.GuideInfoResponse;
import packup.guide.dto.GuideMeResponse;
import packup.guide.dto.MyGuideStatusResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideInfoRepository guideInfoRepository;
    private final GuideApplicationRepository guideApplicationRepository;

    @Transactional(readOnly = true)
    public GuideMeResponse getMyGuide(Long userId) {
        return guideInfoRepository.findByUser_Seq(userId)
                .map(guide -> GuideMeResponse.of(GuideInfoResponse.from(guide)))
                .orElseGet(GuideMeResponse::none);
    }

    @Transactional(readOnly = true)
    public boolean existsMyGuide(Long userId) {
        return guideInfoRepository.existsByUserSeq(userId);
    }

    @Transactional(readOnly = true)
    public MyGuideStatusResponse getMyStatus(final Long userId) {
        final boolean isGuide = guideInfoRepository.existsByUserSeq(userId);

        final Optional<GuideApplication> optionalApplication =
                guideApplicationRepository.findTopByUserSeqOrderByCreatedAtDesc(userId);

        if (optionalApplication.isEmpty()) {
            return MyGuideStatusResponse.builder()
                    .isGuide(isGuide)
                    .application(MyGuideStatusResponse.Application.builder()
                            .exists(false)
                            .statusCode(null)
                            .statusName(null)
                            .rejectReason(null)
                            .canReapply(true)
                            .build())
                    .build();
        }

        final GuideApplication application = optionalApplication.get();
        final Integer statusCodeInt = application.getApplicationStatusCode();
        final GuideApplicationStatus status = GuideApplicationStatus.from(statusCodeInt);

        final String statusCode = status != null ? status.getCode() : null;
        final String statusName = status != null ? status.getName() : null;
        final String rejectReason = application.getRejectReason();
        final boolean canReapply = isReApplicable(status);

        return MyGuideStatusResponse.builder()
                .isGuide(isGuide)
                .application(MyGuideStatusResponse.Application.builder()
                        .exists(true)
                        .statusCode(statusCode)
                        .statusName(statusName)
                        .rejectReason(rejectReason)
                        .canReapply(canReapply)
                        .build())
                .build();
    }

    private boolean isReApplicable(final GuideApplicationStatus status) {
        if (status == null) {
            return true;
        }
        return switch (status) {
            case REJECTED, CANCELED -> true;
            default -> false;
        };
    }
}

