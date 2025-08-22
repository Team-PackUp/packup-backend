package packup.tour.dto.tourBooking.updateDto;

import packup.tour.domain.TourBooking;

/** TourBooking 업데이트 요청의 공통 인터페이스 */

/**
 * 사용예
 * @Transactional
 * public void update(Long bookingSeq, TourBookingUpdateRequest request) {
 *     TourBooking entity = repo.findById(bookingSeq)
 *         .orElseThrow(() -> new NotFoundException("booking not found"));
 *     request.applyTo(entity); // 도메인 메서드 호출 → 더티체킹 → 감사필드 갱신
 * }
 */
public interface TourBookingUpdateRequest {
    /** 전달된 엔티티에 변경을 적용 (더티체킹 유도) */
    void applyTo(TourBooking entity);
}
