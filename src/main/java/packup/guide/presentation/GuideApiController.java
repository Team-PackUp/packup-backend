package packup.guide.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import packup.tour.domain.TourInfo;
import packup.tour.dto.TourCreateRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour/guide")
public class GuideApiController {

    // 가이드 소유 투어 조회
    @GetMapping
    public List<TourInfo> getGuideTours(@AuthenticationPrincipal User user) {
        return null;
    }

    // 투어 등록
    @PostMapping
    public ResponseEntity<?> registerTour(@RequestBody TourCreateRequest request) {
        return null;
    }
}

