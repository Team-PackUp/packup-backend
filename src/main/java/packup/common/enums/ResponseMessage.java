package packup.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    SUCCESS(OK, "요청에 성공했습니다."),
    FAIL(BAD_REQUEST, "요청에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String message() {
        return message;
    }
}
