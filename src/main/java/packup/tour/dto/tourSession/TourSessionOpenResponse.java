package packup.tour.dto.tourSession;

import java.time.LocalDateTime;

public record TourSessionOpenResponse(
        Long seq,
        Long tourSeq,
        LocalDateTime sessionStartTime,
        LocalDateTime sessionEndTime,
        Integer sessionStatusCode,
        LocalDateTime cancelledAt,
        Integer capacity,
        Number bookedCount   // ← Number
) {}
