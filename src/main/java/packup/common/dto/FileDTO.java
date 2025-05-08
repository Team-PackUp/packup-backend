package packup.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileDTO {
    private Long seq;
    private String path;
    private Long userSeq;
    private String encodedName;
    private String realName;
    private String type;
    private LocalDateTime createdAt;
}
