package packup.tour.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import packup.common.enums.YnType;
import packup.tour.enums.TourSessionStatusCode;
import packup.tour.exception.TourSessionException;
import packup.tour.exception.TourSessionExceptionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tour_session")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tour_session SET deleted_flag = 'Y'::yn_enum, updated_at = now() WHERE seq = ?")
public class TourSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_seq", nullable = false)
    private TourInfo tour;

    @Column(name = "session_start_time", nullable = false)
    private LocalDateTime sessionStartTime;

    @Column(name = "session_end_time", nullable = false)
    private LocalDateTime sessionEndTime;

    @Column(name = "session_status_code", nullable = false)
    private Integer sessionStatusCode;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "deleted_flag", columnDefinition = "yn_enum", nullable = false)
    @Builder.Default
    private YnType deletedFlag = YnType.N;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    public TourSessionStatusCode getStatus() {
        return TourSessionStatusCode.fromCode(this.sessionStatusCode);
    }

    public void setStatus(TourSessionStatusCode status) {
        this.sessionStatusCode = status.getCode();
    }

    public void update(LocalDateTime start, LocalDateTime end, TourSessionStatusCode status, Integer maxParticipants) {
        validateTimeRange(start, end);
        validateCapacity(maxParticipants);
        this.sessionStartTime = start;
        this.sessionEndTime = end;
        setStatus(status);
        this.maxParticipants = maxParticipants;
    }

    public void cancel(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
        setStatus(TourSessionStatusCode.CANCELED);
    }

    public void softDelete() { this.deletedFlag = YnType.Y; }

    private static void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new TourSessionException(TourSessionExceptionType.INVALID_TIME_RANGE);
        }
    }

    private static void validateCapacity(Integer capacity) {
        if (capacity == null || capacity < 1) {
            throw new TourSessionException(TourSessionExceptionType.INVALID_CAPACITY);
        }
    }

    public static TourSession of(
            TourInfo tour,
            LocalDateTime start,
            LocalDateTime end,
            TourSessionStatusCode status,
            Integer maxParticipants
    ) {
        validateTimeRange(start, end);
        validateCapacity(maxParticipants);
        return TourSession.builder()
                .tour(tour)
                .sessionStartTime(start)
                .sessionEndTime(end)
                .sessionStatusCode(status.getCode())
                .maxParticipants(maxParticipants)
                .build();
    }

    @PrePersist
    void prePersist() {
        if (deletedFlag == null) deletedFlag = YnType.N;
        if (maxParticipants == null) maxParticipants = 10;
    }
}
