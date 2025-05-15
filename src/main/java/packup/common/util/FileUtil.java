package packup.common.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import packup.common.dto.FileDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class FileUtil {

    @Value("${files.attach_path}")
    private String UPLOAD_DIR;

        private static String nowDate(String format) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(format);
        return dayFormat.format(new Date());
    }

    public FileDTO saveImage(String type, MultipartFile image) throws IOException {
        String today = nowDate("dd");
        String month = nowDate("M");
        String year = nowDate("Y");

        // 이미지를 저장할 디렉토리 경로 설정
        // 실제 저장 경로
        String uploadDir = UPLOAD_DIR + type + "/" + year + "/" + month + "/" + today;
        // db 저장 url
        String uploadDir2 = "/files/" + type + "/" + year + "/" + month + "/" + today;

        // 디렉토리가 없으면 생성
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileExtension = ".";

        // 확장자 추출
        String extension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".") + 1);

        // 파일 물리 이름 + . + 확장자
        String uniqueFileName = UUIDGenerator.generate(type) + fileExtension + extension;

        // 이미지를 저장할 경로 설정
        Path filePath = uploadPath.resolve(uniqueFileName);

        // 이미지 저장
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return FileDTO.builder()
                .path(uploadDir2)
                .encodedName(uniqueFileName)
                .realName(image.getOriginalFilename())
                .type(type)
                .build();
    }
}
