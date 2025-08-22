package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.common.enums.YnType;
import packup.user.domain.UserInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour_review")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tour_review SET deleted_flag = 'Y', updated_at = now() WHERE seq = ?")
public class TourReview {

    /** 투어 후기 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("투어 후기 식별번호")
    private Long seq;

    /** 투어 정보 (FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_seq", nullable = false)
    @Comment("투어 정보 식별번호")
    private TourInfo tour;

    /** 투어 세션 (FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_session_seq", nullable = false)
    @Comment("투어 세션 식별번호")
    private TourSession tourSession;

    /** 작성자 (FK: USER_INFO) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq", nullable = false)
    @Comment("유저 식별번호")
    private UserInfo user;

    /** 후기 점수 (SMALLINT) */
    @Column(name = "review_rating", columnDefinition = "smallint")
    @Comment("후기 점수")
    private Integer reviewRating;

    /** 후기 코멘트 (TEXT) */
    @Column(name = "review_comment", columnDefinition = "TEXT")
    @Comment("후기 코멘트")
    private String reviewComment;

    /** 삭제 여부 (Y/N) */
    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_flag", columnDefinition = "public.yn_enum")
    @Comment("삭제 여부")
    @Builder.Default
    private YnType deletedFlag = YnType.N;

    /** 등록일시 */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Comment("등록일시")
    private LocalDateTime createdAt;

    /** 수정일시 */
    @LastModifiedDate
    @Column(name = "updated_at")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    /* ===================== 도메인 메서드 ===================== */

    /** 후기 내용/점수 수정 */
    public void update(Integer rating, String comment) {
        this.reviewRating = rating;
        this.reviewComment = comment;
    }

    /** 명시적 소프트 삭제가 필요할 때 */
    public void softDelete() {
        this.deletedFlag = YnType.Y;
    }

    /** 투어/세션 일관성 체크: 세션이 해당 투어에 속하는지 확인 */
    public void assertBelongsToTour() {
        if (tourSession != null && tourSession.getTour() != null && !tourSession.getTour().getSeq().equals(tour.getSeq())) {
            throw new IllegalArgumentException("세션이 지정된 투어에 속하지 않습니다.");
        }
    }
}
