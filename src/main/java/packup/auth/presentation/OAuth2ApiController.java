package packup.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.service.OAuth2Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuth2ApiController {

    private final OAuth2Service oAuth2Service;

}
