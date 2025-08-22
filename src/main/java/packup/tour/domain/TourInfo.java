package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.common.domain.BaseEntity;
import packup.common.enums.YnType;
import packup.guide.domain.GuideInfo;
import packup.tour.enums.TourStatusCode;
import packup.tour.enums.TourStatusCodeConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * TourInfo (투어 정보 엔티티)
 *
 * 투어 상품의 핵심 정보를 보유하는 도메인 객체.
 * 가이드가 등록한 투어에 대해 기간, 모집 인원, 설명, 상태 등을 포함하며
 * 투어 상태 변경이나 사용자 신청 등 투어 생애주기를 관리하는 기본 단위.
 * </pre>
 *
 * <p><b>상태 흐름 예시:</b>
 * TEMP → RECRUITING → RECRUITED → READY → ONGOING → FINISHED
 * </p>
 *
 * @author SBLEE
 * @since 2025.05.12
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "tour_info")
@EntityListeners(AuditingEntityListener.class)
public class TourInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("투어 정보 식별번호")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guide_seq", nullable = false)
    @Comment("가이드 사용자 식별번호")
    private GuideInfo guide;

    @Column(name = "tour_title", length = 255)
    @Comment("투어 제목")
    private String tourTitle;

    @Column(name = "tour_introduce", columnDefinition = "TEXT")
    @Comment("투어 소개")
    private String tourIntroduce;

    @Column(name = "tour_included_content", columnDefinition = "TEXT")
    @Comment("투어 포함 콘텐츠")
    private String tourIncludedContent;

    @Column(name = "tour_excluded_content", columnDefinition = "TEXT")
    @Comment("투어 미포함 콘텐츠")
    private String tourExcludedContent;

    @Column(name = "tour_notes", length = 255)
    @Comment("투어 참고사항")
    private String tourNotes;

    @Column(name = "tour_location_code")
    @Comment("투어 지역 코드")
    private Long tourLocationCode;

    @Column(name = "tour_thumbnail_url", columnDefinition = "TEXT")
    @Comment("투어 섬네일 URL")
    private String tourThumbnailUrl;

    @Column(name = "tour_price", nullable = false)
    @Comment("투어 가격")
    private Long tourPrice;

    @Column(name = "min_head_count")
    @Comment("최소 인원 수")
    private Integer minHeadCount;

    @Column(name = "max_head_count")
    @Comment("최대 인원 수")
    private Integer maxHeadCount;

    @Column(name = "meet_up_address", columnDefinition = "TEXT")
    @Comment("모임 주소")
    private String meetUpAddress;

    @Column(name = "meet_up_lat", precision = 9, scale = 6)
    @Comment("모임 위도좌표")
    private BigDecimal meetUpLat;

    @Column(name = "meet_up_lng", precision = 9, scale = 6)
    @Comment("모임 경도좌표")
    private BigDecimal meetUpLng;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_service_flag", columnDefinition = "public.yn_enum")
    @Comment("차량 운송 여부")
    @Builder.Default
    private YnType transportServiceFlag = YnType.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "private_flag", columnDefinition = "public.yn_enum")
    @Comment("프라이빗 제공여부")
    @Builder.Default
    private YnType privateFlag = YnType.N;

    @Column(name = "private_price")
    @Comment("프라이빗 가격")
    private Long privatePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "adult_content_flag", columnDefinition = "public.yn_enum")
    @Comment("성인 콘텐츠 포함 여부")
    @Builder.Default
    private YnType adultContentFlag = YnType.N;

    /**
     * 투어 상태 코드 (enum 기반, 문자열 저장)
     * - TEMP: 임시저장
     * - RECRUITING: 모집중
     * - RECRUITED: 모집완료
     * - READY: 출발대기
     * - ONGOING: 투어중
     * - FINISHED: 종료
     */
    @Convert(converter = TourStatusCodeConverter.class)
    @Column(name = "tour_status_code")
    @Comment("투어 상태 코드")
    private TourStatusCode tourStatusCode;

    @Column(name = "approval_admin_seq")
    @Comment("승인 관리자 식별번호")
    private Integer approvalAdminSeq;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    @Comment("반려사유")
    private String rejectReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_flag", columnDefinition = "public.yn_enum")
    @Comment("삭제 여부")
    @Builder.Default
    private YnType deletedFlag = YnType.N;

    @Column(name = "memo", length = 255)
    @Comment("가이드 관리용 메모")
    private String memo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Comment("등록일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    /** -------------------- 1 : N = TourInfo : TourActivity -------------------- */
    @OneToMany(
            mappedBy = "tour",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("activityOrder ASC")
    @Comment("투어 체험 목록")
    @Builder.Default
    private List<TourActivity> activities = new ArrayList<>();

    //** 도메인 업데이트 메서드 */
    public void update(String tourTitle,
                       String tourIntroduce,
                       String tourIncludedContent,
                       String tourExcludedContent,
                       String tourNotes,
                       Long tourLocationCode,
                       String tourThumbnailUrl,
                       Long tourPrice,
                       Integer minHeadCount,
                       Integer maxHeadCount,
                       String meetUpAddress,
                       BigDecimal meetUpLat,
                       BigDecimal meetUpLng,
                       YnType transportServiceFlag,
                       YnType privateFlag,
                       Long privatePrice,
                       YnType adultContentFlag,
                       TourStatusCode tourStatusCode,
                       Integer approvalAdminSeq,
                       String rejectReason,
                       YnType deletedFlag,
                       String memo) {

        this.tourTitle = tourTitle;
        this.tourIntroduce = tourIntroduce;
        this.tourIncludedContent = tourIncludedContent;
        this.tourExcludedContent = tourExcludedContent;
        this.tourNotes = tourNotes;
        this.tourLocationCode = tourLocationCode;
        this.tourThumbnailUrl = tourThumbnailUrl;
        this.tourPrice = tourPrice;
        this.minHeadCount = minHeadCount;
        this.maxHeadCount = maxHeadCount;
        this.meetUpAddress = meetUpAddress;
        this.meetUpLat = meetUpLat;
        this.meetUpLng = meetUpLng;
        this.transportServiceFlag = transportServiceFlag;
        this.privateFlag = privateFlag;
        this.privatePrice = privatePrice;
        this.adultContentFlag = adultContentFlag;
        this.tourStatusCode = tourStatusCode;
        this.approvalAdminSeq = approvalAdminSeq;
        this.rejectReason = rejectReason;
        this.deletedFlag = deletedFlag;
        this.memo = memo;
    }
}
