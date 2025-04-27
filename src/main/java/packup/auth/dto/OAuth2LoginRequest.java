package packup.auth.dto;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class OAuth2LoginRequest {

    @JsonProperty("access_token")
    private String accessToken;
}