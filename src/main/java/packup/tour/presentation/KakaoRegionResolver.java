package packup.tour.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KakaoRegionResolver implements RegionResolver {

    @Value("${ext.kakao.rest-api-key}")
    private String kakaoKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<String> resolveSidoName(double lat, double lng) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + lng + "&y=" + lat;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoResp> resp = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoResp.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) return Optional.empty();

        List<KakaoResp.Document> docs = resp.getBody().documents;
        if (docs == null || docs.isEmpty()) return Optional.empty();

        String name = docs.get(0).region_1depth_name;
        return Optional.ofNullable(name);
    }

    public static class KakaoResp {
        public List<Document> documents;
        public static class Document {
            public String region_type;
            public String region_1depth_name; // "서울특별시" 등
            public String region_2depth_name;
        }
    }
}