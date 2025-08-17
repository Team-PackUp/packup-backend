package packup.guide.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.guide.dto.GuideApplicationCreateResponse;
import packup.guide.dto.GuideMeResponse;
import packup.guide.dto.MyGuideStatusResponse;
import packup.guide.service.GuideService;
import packup.tour.domain.TourInfo;
import packup.tour.dto.TourInfoCreateRequest;

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
}

