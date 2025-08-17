package packup.guide.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.guide.dto.GuideMeResponse;
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

    @GetMapping("/me")
    public ResultModel<GuideMeResponse> me(@Auth Long memberId) {
        return ResultModel.success(guideService.getMyGuide(memberId));
    }

    @GetMapping("/me/exists")
    public ResultModel<Boolean> exists(@Auth Long memberId) {
        return ResultModel.success(guideService.existsMyGuide(memberId));
    }
}

