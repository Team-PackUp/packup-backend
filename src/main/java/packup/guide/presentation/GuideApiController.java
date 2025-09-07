package packup.guide.presentation;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import packup.auth.annotation.Auth;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;
import packup.guide.domain.GuideInfo;
import packup.guide.dto.*;
import packup.guide.dto.guideInfo.GuideInfoResponse;
import packup.guide.service.GuideService;
import packup.tour.domain.TourInfo;
import packup.tour.dto.tourInfo.TourInfoCreateRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guide")
public class GuideApiController {

    private final GuideService guideService;

    // 가이드 소유 투어 조회
    @GetMapping
    public List<TourInfo> getGuideTours(@AuthenticationPrincipal User user) {
        return null;
    }

    // 투어 등록
    @PostMapping
    public ResponseEntity<?> registerTour(@RequestBody TourInfoCreateRequest request) {
        return null;
    }

    @GetMapping("/list")
    public ResultModel<PageDTO<GuideInfoResponse>> list(int page, int size) {
        return ResultModel.success(guideService.list(page, size));
    }

    @GetMapping("/detail/{guideSeq}")
    public ResultModel<GuideInfoResponse> getGuideDetail(@PathVariable Long guideSeq) {
        return ResultModel.success(guideService.getGuideDetail(guideSeq));
    }

    @GetMapping("/me")
    public ResultModel<GuideMeResponse> me(@Auth Long memberId) {
        return ResultModel.success(guideService.getMyGuide(memberId));
    }

    @GetMapping("/me/exists")
    public ResultModel<Boolean> exists(@Auth Long memberId) {
        return ResultModel.success(guideService.existsMyGuide(memberId));
    }

    @GetMapping("/me/status")
    public ResultModel<MyGuideStatusResponse> myStatus(@Auth Long memberId) {
        return ResultModel.success(guideService.getMyStatus(memberId));
    }

    @PostMapping(
            value = "/application",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResultModel<GuideApplicationCreateResponse> submitApplication(
            @RequestPart("idImage") MultipartFile idImage,
            @RequestPart("selfIntro") String selfIntro,
            @Auth Long memberId
    ) {

        return ResultModel.success(guideService.createApplication(memberId, selfIntro, idImage));
    }

    @GetMapping("/intro/me")
    public ResultModel<GuideIntroResponse> getMyIntro(@Auth Long memberId) {
        return ResultModel.success(
                GuideIntroResponse.of(guideService.fetchMyIntro(memberId))
        );
    }

    @PutMapping("/intro/me")
    public ResultModel<GuideIntroResponse> upsertMyIntro(
            @Auth Long memberId,
            @Valid @RequestBody GuideIntroRequest request
    ) {
        return ResultModel.success(
                GuideIntroResponse.of(
                        guideService.upsertIntro(memberId, request)
                )
        );
    }



}

