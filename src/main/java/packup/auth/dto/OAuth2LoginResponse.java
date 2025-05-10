package packup.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2LoginResponse {
    private String accessToken;
    private String refreshToken;
}