package packup.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ImageDTO {
    private String path;
    private Long userSeq;
    private String encodedName;
    private String realName;
}
