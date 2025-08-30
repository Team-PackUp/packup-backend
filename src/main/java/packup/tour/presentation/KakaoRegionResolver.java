package packup.tour.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class KakaoRegionResolver implements RegionResolver {

    @Value("${ext.kakao.enabled:false}")
    private boolean enabled;

    @Value("${ext.kakao.rest-api-key:}")
    private String kakaoKey;

    private final RestTemplate restTemplate;

    public KakaoRegionResolver() {
        this.restTemplate = new RestTemplate();
        // ★ 에러라도 예외 던지지 않게
        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override public boolean hasError(ClientHttpResponse response) throws IOException { return false; }
        });
    }

    @Override
    public Optional<String> resolveSidoName(double lat, double lng) {
        if (!enabled) return Optional.empty();
        if (kakaoKey == null || kakaoKey.isBlank()) return Optional.empty();

        try {
            String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + lng + "&y=" + lat;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoResp> resp = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoResp.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                return Optional.empty();
            }

            List<KakaoResp.Document> docs = resp.getBody().documents;
            if (docs == null || docs.isEmpty()) return Optional.empty();

            String name = docs.get(0).region_1depth_name; // 예: "서울특별시"
            return Optional.ofNullable(name);
        } catch (Exception ignore) {
            return Optional.empty();
        }
    }

    public static class KakaoResp {
        public List<Document> documents;
        public static class Document {
            public String region_type;
            public String region_1depth_name;
            public String region_2depth_name;
        }
    }
}
