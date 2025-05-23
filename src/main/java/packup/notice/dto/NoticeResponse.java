package packup.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Setter
@Getter
public class NoticeResponse {
    private Long seq;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
