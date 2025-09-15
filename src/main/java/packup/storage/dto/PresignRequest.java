package packup.storage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PresignRequest {
    private String filename;     // ex) cover.jpg
    private String contentType;  // ex) image/jpeg
    private String prefix;       // ex) "tour/gallery/"
}
