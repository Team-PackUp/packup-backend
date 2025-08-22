package packup.tour.domain;

import jakarta.persistence.*;
        import lombok.*;
        import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour_activity_thumbnail")
@EntityListeners(AuditingEntityListener.class)
public class TourActivityThumbnail {

    /** 투어 체험 섬네일 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("투어 체험 섬네일 식별번호")
    private Long seq;

    /** 투어 체험 (FK, TOUR_ACTIVITY) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_activity_seq", nullable = false)
    @Comment("투어 체험 식별번호")
    private TourActivity tourActivity;

    /** 섬네일 순서 */
    @Column(name = "thumbnail_order", nullable = false)
    @Comment("섬네일 순서")
    private Integer thumbnailOrder;

    /** 섬네일 이미지 URL */
    @Column(name = "thumbnail_image_url", columnDefinition = "TEXT", nullable = false)
    @Comment("섬네일 이미지 URL")
    private String thumbnailImageUrl;

    /** 등록일시 */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Comment("등록일시")
    private LocalDateTime createdAt;

    /**
     * 엔티티 수정 메서드
     */
    public void update(Integer thumbnailOrder, String thumbnailImageUrl) {
        this.thumbnailOrder = thumbnailOrder;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}
