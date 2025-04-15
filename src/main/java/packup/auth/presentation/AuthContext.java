package packup.auth.presentation;

import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Setter
@Component
@RequestScope
public class AuthContext {

    private Long memberId;

    public Long memberId() {
        return memberId;
    }

}
