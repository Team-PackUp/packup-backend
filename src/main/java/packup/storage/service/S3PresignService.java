package packup.storage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class S3PresignService {

    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    // 허용되는 이미지 MIME
    private static final Set<String> ALLOWED_CT = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif", "image/heic", "image/heif"
    );

    // 확장자 -> MIME 간단 매핑
    private static final Map<String, String> EXT_TO_CT = Map.ofEntries(
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("png", "image/png"),
            Map.entry("webp", "image/webp"),
            Map.entry("gif", "image/gif"),
            Map.entry("heic", "image/heic"),
            Map.entry("heif", "image/heif")
    );

    // (선택) prefix 화이트리스트
    private static final List<String> ALLOWED_PREFIXES = List.of(
            "uploads/", "tour/gallery/", "tour/title/"
    );

    public String buildKey(String prefix, String filename) {
        String ext = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.') + 1))
                .orElse("bin")
                .toLowerCase();

        String p = (prefix == null || prefix.isBlank()) ? "uploads/" : prefix.trim();
        // 허용 prefix만 사용(안전)
        boolean allowed = ALLOWED_PREFIXES.stream().anyMatch(p::startsWith);
        if (!allowed) p = "uploads/";

        if (!p.endsWith("/")) p += "/";

        String date = LocalDate.now(ZoneOffset.UTC).toString(); // e.g. 2025-09-14
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return p + date + "/" + uuid + "." + ext; // e.g. tour/gallery/2025-09-14/uuid.jpg
    }

    public PresignedPutObjectRequest presignPut(String key, String contentTypeIn) {
        // contentType 보정: 입력값 -> key 확장자 추론
        String ct = normalizeContentType(contentTypeIn, key);

        if (!ALLOWED_CT.contains(ct)) {
            throw new IllegalArgumentException("Unsupported Content-Type: " + ct);
        }

        var put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(ct)
                .build();

        var req = PutObjectPresignRequest.builder()
                .putObjectRequest(put)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        return presigner.presignPutObject(req);
    }

    /** presigned GET URL (보기용) */
    public String presignGetUrl(String key, int ttlMinutes) {
        var get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        var req = GetObjectPresignRequest.builder()
                .getObjectRequest(get)
                .signatureDuration(Duration.ofMinutes(Math.max(1, ttlMinutes)))
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(req);
        return presigned.url().toString();
    }

    /** 파일명 기준 MIME 추론 (컨트롤러에서 사용) */
    public static String inferContentTypeByFilename(String filename) {
        String ext = (filename != null && filename.contains("."))
                ? filename.substring(filename.lastIndexOf('.') + 1).toLowerCase()
                : "";
        return EXT_TO_CT.getOrDefault(ext, "application/octet-stream");
    }

    /** 입력 MIME이 비었거나 허용 외이면 key 확장자로 재추론 후 정규화 */
    private static String normalizeContentType(String contentTypeIn, String key) {
        String ct = (contentTypeIn == null) ? "" : contentTypeIn.trim().toLowerCase();
        if (!ct.isBlank() && ALLOWED_CT.contains(ct)) {
            return ct;
        }
        // key의 확장자에서 보정
        String keyExt = (key != null && key.contains(".")) ? key.substring(key.lastIndexOf('.') + 1).toLowerCase() : "";
        String guessed = EXT_TO_CT.get(keyExt);
        if (guessed != null) return guessed;

        // 최후: 그대로 반환하여 예외 처리로 이어지게
        return ct.isBlank() ? "application/octet-stream" : ct;
    }
}
