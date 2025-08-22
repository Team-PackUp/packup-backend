package packup.tour.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
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
@Table(name = "tour_session")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tour_session SET deleted_flag = 'Y', updated_at = now() WHERE seq = ?")
public class TourSession {

    /** 투어 세션 식별번호 (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Comment("투어 세션 식별번호")
    private Long seq;

    /** 투어 정보 (FK, TOUR_INFO) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_seq", nullable = false)
    @Comment("투어 정보 식별번호")
    private TourInfo tour;

    /** 세션 시작 시간 */
    @Column(name = "session_start_time")
    @Comment("세션 시작 시간")
    private LocalDateTime sessionStartTime;

    /** 세션 종료 시간 */
    @Column(name = "session_end_time")
    @Comment("세션 종료 시간")
    private LocalDateTime sessionEndTime;

    /** 세션 상태 코드 (int) */
    @Column(name = "session_status_code")
    @Comment("세션 상태 코드")
    private Integer sessionStatusCode;

    /** 세션 중단 시간 */
    @Column(name = "cancelled_at")
    @Comment("세션 중단 시간")
    private LocalDateTime cancelledAt;

    /** 삭제 여부 (Y/N) */
    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_flag", columnDefinition = "public.yn_enum")
    @Comment("삭제 여부")
    @Builder.Default
    private YnType deletedFlag = YnType.N;

    /** 등록 일시 */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Comment("등록 일시")
    private LocalDateTime createdAt;

    /** 수정 일시 */
    @LastModifiedDate
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;

    /* ===================== 도메인 메서드 ===================== */

    /** 세션 시간/상태 변경 */
    public void update(LocalDateTime start, LocalDateTime end, Integer statusCode) {
        this.sessionStartTime = start;
        this.sessionEndTime = end;
        this.sessionStatusCode = statusCode;
    }

    /** 세션 중단 처리 */
    public void cancel(LocalDateTime cancelledAt, Integer cancelledStatusCode) {
        this.cancelledAt = cancelledAt;
        this.sessionStatusCode = cancelledStatusCode;
    }

    /** 소프트 삭제 명시 처리 (직접 토글이 필요할 때) */
    public void softDelete() {
        this.deletedFlag = YnType.Y;
    }
}
