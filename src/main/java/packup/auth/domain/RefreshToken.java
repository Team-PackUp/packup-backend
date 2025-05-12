package packup.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import packup.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 512)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public void updateToken(String newToken, LocalDateTime newExpiresAt) {
        this.token = newToken;
        this.expiresAt = newExpiresAt;
    }
}
