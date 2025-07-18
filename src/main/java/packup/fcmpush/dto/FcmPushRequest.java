package packup.fcmpush.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FcmPushRequest {
    private List<Long> userSeqList;
    private String title;
    private String body;
    private DeepLink deepLink;
}
