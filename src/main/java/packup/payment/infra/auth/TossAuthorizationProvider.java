package packup.payment.infra.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Component
public class TossAuthorizationProvider {

    private final String authHeader;

    public TossAuthorizationProvider(@Value("${payment.toss.secret-key}") String secretKey) {
        this.authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
    }

}