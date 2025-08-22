package packup.tour.dto.tourReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.common.enums.YnType;
import packup.tour.domain.TourReview;
import packup.tour.dto.tourInfo.TourInfoResponse;
import packup.tour.dto.tourSession.TourSessionResponse;
import packup.user.dto.UserInfoResponse;

import java.time.LocalDateTime;

/**
 * TourReview 응답 DTO
 * - 연관관계는 ID(tourSeq, tourSessionSeq, userSeq)만 노출
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourReviewResponse {

    /** 투어 후기 식별번호 (PK) */
    private Long seq;

    /** 연관 식별자 */
    private TourInfoResponse tour;
    private TourSessionResponse tourSession;
    private UserInfoResponse user;

    /** 후기 내용 */
    private Integer reviewRating;
    private String reviewComment;

    /** 소프트 삭제 여부 */
    private YnType deletedFlag;

    /** 감사 필드 */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 엔티티 → DTO 매핑 */
    public static TourReviewResponse from(TourReview tourReview) {
        return TourReviewResponse.builder()
                .seq(tourReview.getSeq())
                .tour(TourInfoResponse.from(tourReview.getTour()))
                .tourSession(TourSessionResponse.from(tourReview.getTourSession()))
                .user(UserInfoResponse.of(tourReview.getUser()))
                .reviewRating(tourReview.getReviewRating())
                .reviewComment(tourReview.getReviewComment())
                .deletedFlag(tourReview.getDeletedFlag())
                .createdAt(tourReview.getCreatedAt())
                .updatedAt(tourReview.getUpdatedAt())
                .build();
    }
}
