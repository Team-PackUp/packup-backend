package packup.airecommend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import packup.tour.domain.TourInfo;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.tour.dto.TourInfoResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiRecommendService {

    private final TourInfoRepository tourInfoRepository;

    public List<TourInfoResponse> popularTour(int count) {

        List<TourInfo> latestTour = new ArrayList<>(
                tourInfoRepository.findLatest(LocalDate.now(), PageRequest.of(0, 50))
                        .getContent()); //getContent or toList는 읽기 전용이라 복사 데이터 생성 후 shuffle


        if(latestTour.size() > 0) {
            Collections.shuffle(latestTour);
        }

        return latestTour.stream()
                .limit(count)
                .map(TourInfoResponse::from)
                .toList();
    }
}
