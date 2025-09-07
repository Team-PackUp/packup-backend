package packup.guide.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import packup.alert.dto.AlertResponse;
import packup.common.dto.PageDTO;
import packup.guide.domain.GuideApplication;
import packup.guide.domain.GuideApplicationStatus;
import packup.guide.domain.GuideInfo;
import packup.guide.domain.repository.GuideApplicationRepository;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.guide.dto.*;
import packup.guide.dto.guideInfo.GuideInfoResponse;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.exception.UserException;
import packup.user.exception.UserExceptionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideInfoRepository guideInfoRepository;
    private final GuideApplicationRepository guideApplicationRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public GuideMeResponse getMyGuide(Long userId) {
        return guideInfoRepository.findByUser_Seq(userId)
                .map(guide -> GuideMeResponse.of(GuideInfoResponse.from(guide)))
                .orElseGet(GuideMeResponse::none);
    }

    @Transactional(readOnly = true)
    public PageDTO<GuideInfoResponse> list(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<GuideInfo> guideInfoResponseList = guideInfoRepository.findAll(pageable);

        return PageDTO.<GuideInfoResponse>builder()
                .objectList(
                        guideInfoRepository.findAll(pageable)
                        .stream().map(GuideInfoResponse::from).toList()
                )
                .totalPage(guideInfoResponseList.getTotalPages())
                .totalElements(guideInfoResponseList.getTotalElements())
                .curPage(guideInfoResponseList.getNumber())
                .nextPageFlag(guideInfoResponseList.hasNext())
                .build();

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

    @Transactional
    public GuideApplicationCreateResponse createApplication(
            Long memberId,
            String selfIntro,
            MultipartFile idImage
    ) {
        String fakeUrl = toFakeUrl(idImage);

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        GuideApplication entity = GuideApplication.builder()
                .user(userInfo)
                .guideIdcardImageUrl(fakeUrl)
                .guideIntroduce(selfIntro)
                .applicationRegisteredAt(LocalDateTime.now())
                .applicationStatusCode(60001)
                .build();

        GuideApplication saved = guideApplicationRepository.save(entity);

        return new GuideApplicationCreateResponse(
                saved.getSeq(),
                saved.getGuideIdcardImageUrl(),
                saved.getGuideIntroduce()
        );
    }

    @Transactional
    public GuideInfo upsertIntro(Long userSeq, GuideIntroRequest req) {
        final UserInfo user = userInfoRepository.findById(userSeq)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        final short years = (req.years() == null ? 0 : req.years().shortValue());
        final String roleSummary = safe(req.roleSummary());
        final String expertise   = safe(req.expertise());
        final String achievement = safe(req.achievement());
        final String summary     = safe(req.summary());

        return guideInfoRepository.findByUser_Seq(userSeq)
                .map(e -> {
                    e.applyIntro(years, roleSummary, expertise, achievement, summary);
                    return e;
                })
                .orElseGet(() -> guideInfoRepository.save(
                        GuideInfo.builder()
                                .user(user)
                                .years(years)
                                .roleSummary(roleSummary)
                                .expertise(expertise)
                                .achievement(achievement)
                                .summary(summary)
                                .build()
                ));
    }

    @Transactional
    public GuideInfo fetchMyIntro(Long userSeq) {
        final UserInfo user = userInfoRepository.findById(userSeq)
                .orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_MEMBER));

        return guideInfoRepository.findByUser_Seq(userSeq)
                .orElseGet(() -> guideInfoRepository.save(
                        GuideInfo.builder()
                                .user(user)
                                .years((short)0)
                                .roleSummary("")
                                .expertise("")
                                .achievement("")
                                .summary("")
                                .build()
                ));
    }

    private String toFakeUrl(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String ext = (filename != null && filename.contains(".")) ?
                filename.substring(filename.lastIndexOf('.') + 1) : "bin";
        return "https://packup.aws.s3.temptemp.com/guide-id/" +
                UUID.randomUUID() + (ext.isEmpty() ? "" : ("." + ext));
    }

    private String safe(String s) { return s == null ? "" : s; }
}

