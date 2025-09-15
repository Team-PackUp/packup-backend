package packup.storage.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.common.dto.ResultModel;
import packup.storage.dto.PresignRequest;
import packup.storage.dto.PresignResponse;
import packup.storage.service.S3PresignService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files") // 클라 경로에 맞추세요: "/files" 또는 "/api/files"
public class FileController {

    private final S3PresignService service;

    @PostMapping("/presign")
    public ResultModel<PresignResponse> presign(@RequestBody PresignRequest body) {
        // 1) 입력값 디폴트/보정
        final String filename = (body.getFilename() == null || body.getFilename().isBlank())
                ? "file.bin" : body.getFilename().trim();
        final String prefix = (body.getPrefix() == null) ? "" : body.getPrefix().trim();

        // contentType 우선순위: 요청값 -> 확장자 추론 -> (최후) application/octet-stream
        String contentType = body.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = S3PresignService.inferContentTypeByFilename(filename);
        }

        // 2) S3 키 생성 + presign
        final String key = service.buildKey(prefix, filename);
        final var presigned = service.presignPut(key, contentType);

        // 3) 업로드 시 필요한 헤더(현재 Content-Type만 필요)
        Map<String, String> headers = Map.of("Content-Type", contentType);

        // 4) ResultModel 래핑으로 반환 (클라가 기대하는 포맷)
        return ResultModel.success(new PresignResponse(presigned.url().toString(), key, headers));
    }

    /**
     * (옵션) 보기용 URL 발급: 키 배열 -> presigned GET URL 맵
     * CloudFront 없거나, 인증된 사용자에게만 노출하고 싶을 때 사용.
     */
    @PostMapping("/view-urls")
    public ResultModel<Map<String, String>> viewUrls(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Object> rawKeys = (List<Object>) body.getOrDefault("keys", List.of());
        int ttlMinutes = ((Number) body.getOrDefault("ttlMinutes", 30)).intValue();

        Map<String, String> out = new LinkedHashMap<>();
        for (Object k : rawKeys) {
            if (k == null) continue;
            String key = k.toString();
            String url = service.presignGetUrl(key, ttlMinutes);
            out.put(key, url);
        }
        return ResultModel.success(out);
    }
}
