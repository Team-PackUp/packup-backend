package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.common.enums.YnType;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour_activity")
@EntityListeners(AuditingEntityListener.class)
public class TourActivity {

    /** 투어 체험 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("투어 체험 식별번호")
    private Long seq;

    /** 투어 정보 (FK, TOUR_INFO) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_seq", nullable = false)
    @Comment("투어 정보 식별번호")
    private TourInfo tour;

    /** 체험 순서 */
    @Column(name = "activity_order", nullable = false)
    @Comment("체험 순서")
    private Integer activityOrder;

    /** 체험 제목 */
    @Column(name = "activity_title", length = 255, nullable = false)
    @Comment("체험 제목")
    private String activityTitle;

    /** 체험 소개 */
    @Column(name = "activity_introduce", length = 255)
    @Comment("체험 소개")
    private String activityIntroduce;

    /** 체험 소요시간 (분) */
    @Column(name = "activity_duration_minute")
    @Comment("체험 소요시간 (분)")
    private Integer activityDurationMinute;

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

    /**
     * 체험 정보 수정 메서드
     */
    public void update(Integer activityOrder,
                       String activityTitle,
                       String activityIntroduce,
                       Integer activityDurationMinute,
                       YnType deletedFlag) {
        this.activityOrder = activityOrder;
        this.activityTitle = activityTitle;
        this.activityIntroduce = activityIntroduce;
        this.activityDurationMinute = activityDurationMinute;
        this.deletedFlag = deletedFlag;
    }
}
