package packup.tour.dto.tourReview;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.tour.domain.TourInfo;
import packup.tour.domain.TourReview;
import packup.tour.domain.TourSession;
import packup.user.domain.UserInfo;

/**
 * TourReview 생성 요청 DTO
 * - deletedFlag, createdAt, updatedAt 은 엔티티 기본/감사 설정 사용
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourReviewCreateRequest {

    /** 투어 정보 식별번호 (FK) */
    @NotNull(message = "tourSeq는 필수입니다.")
    private Long tourSeq;

    /** 투어 세션 식별번호 (FK) */
    @NotNull(message = "tourSessionSeq는 필수입니다.")
    private Long tourSessionSeq;

    /** 작성자 식별번호 (FK: USER_INFO)
     *  - 실제 구현에서는 인증 컨텍스트에서 가져오길 권장합니다. */
    @NotNull(message = "userSeq는 필수입니다.")
    private Long userSeq;

    /** 후기 점수 (범위는 정책에 맞게 조정) */
    @NotNull(message = "reviewRating은 필수입니다.")
    @Min(value = 0, message = "reviewRating은 0 이상이어야 합니다.")
    @Max(value = 5, message = "reviewRating은 5 이하여야 합니다.")
    private Integer reviewRating;

    /** 후기 코멘트 */
    @NotBlank(message = "reviewComment는 필수입니다.")
    private String reviewComment;

    /**
     * 사전에 조회한 연관 엔티티로 신규 TourReview 엔티티 생성
     * - 세션이 해당 투어에 속하는지 도메인 규칙 검증(assertBelongsToTour) 수행
     */
    public TourReview toEntity(TourInfo tour, TourSession tourSession, UserInfo user) {
        TourReview review = TourReview.builder()
                .tour(tour)
                .tourSession(tourSession)
                .user(user)
                .reviewRating(this.reviewRating)
                .reviewComment(this.reviewComment)
                .build();

        // 투어/세션 일관성 검증
        review.assertBelongsToTour();
        return review;
    }
}
