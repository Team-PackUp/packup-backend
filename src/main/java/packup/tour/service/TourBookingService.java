package packup.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.enums.YnType;
import packup.tour.domain.TourBooking;
import packup.tour.domain.TourSession;
import packup.tour.domain.repository.TourBookingRepository;
import packup.tour.domain.repository.TourSessionRepository;
import packup.tour.dto.tourBooking.TourBookingCreateRequest;
import packup.tour.dto.tourBooking.TourBookingResponse;
import packup.tour.enums.TourSessionStatusCode;
import packup.tour.exception.TourSessionException;
import packup.tour.exception.TourSessionExceptionType;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;

@Service
@RequiredArgsConstructor
public class TourBookingService {

    private final TourSessionRepository tourSessionRepository;
    private final TourBookingRepository tourBookingRepository;
    private final UserInfoRepository userInfoRepository;

    private static final long BOOKING_STATUS_BOOKED = 1001L;

    @Transactional
    public TourBookingResponse createBooking(Long userSeq, TourBookingCreateRequest req) {
        UserInfo user = userInfoRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userSeq));

        TourSession session = tourSessionRepository.findByIdForUpdate(req.getTourSessionSeq())
                .orElseThrow(() -> new TourSessionException(TourSessionExceptionType.NOT_FOUND_SESSION));

        Integer raw = (session.getSessionStatusCode() == null) ? null : session.getSessionStatusCode().intValue();
        if (raw == null) throw new TourSessionException(TourSessionExceptionType.NOT_FOUND_SESSION);

        TourSessionStatusCode status = TourSessionStatusCode.fromCode(raw);
        if (status != TourSessionStatusCode.OPEN) {
            throw new TourSessionException(TourSessionExceptionType.NOT_FOUND_SESSION);
        }

        int capacity = (session.getMaxParticipants() != null) ? session.getMaxParticipants() : Integer.MAX_VALUE;
        long alreadyBooked = tourBookingRepository.countBookedHead(session.getSeq());
        long requestHead =
                (long) req.getBookingAdultCount() +
                        (req.getBookingKidsCount() == null ? 0L : req.getBookingKidsCount());

        if (alreadyBooked + requestHead > capacity) {
            throw new TourSessionException(TourSessionExceptionType.INVALID_CAPACITY);
        }

        TourBooking booking = TourBooking.builder()
                .tourSession(session)
                .user(user)
                .paymentSeq(null)
                .bookingStatusCode(BOOKING_STATUS_BOOKED)
                .bookingAdultCount(req.getBookingAdultCount())
                .bookingKidsCount(req.getBookingKidsCount() == null ? 0 : req.getBookingKidsCount())
                .bookingPrivateFlag("Y".equalsIgnoreCase(req.getBookingPrivateFlag()) ? YnType.Y : YnType.N)
                .build();

        TourBooking saved = tourBookingRepository.save(booking);
        return TourBookingResponse.from(saved);
    }
}
