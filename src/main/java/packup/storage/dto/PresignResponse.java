package packup.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PresignResponse {
    private String uploadUrl;
    private String key;
    private Map<String, String> headers;
}
