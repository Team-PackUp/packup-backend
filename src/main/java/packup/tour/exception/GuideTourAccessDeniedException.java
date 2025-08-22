package packup.tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class GuideTourAccessDeniedException extends  RuntimeException {
    public GuideTourAccessDeniedException(Long tourSeq) {
        super("본인이 등록한 투어만 수정/삭제할 수 있습니다.");
    }
}
