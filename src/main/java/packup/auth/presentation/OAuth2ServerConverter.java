package packup.auth.presentation;

import org.springframework.core.convert.converter.Converter;
import packup.auth.domain.OAuth2ServerType;

public class OAuth2ServerConverter implements Converter<String, OAuth2ServerType> {

    @Override
    public OAuth2ServerType convert(String source) {
        return OAuth2ServerType.fromName(source);
    }
}
