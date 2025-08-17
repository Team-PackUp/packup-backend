package packup.guide.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.guide.dto.guideInfo.GuideInfoResponse;
import packup.guide.dto.GuideMeResponse;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideInfoRepository guideInfoRepository;

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
}

