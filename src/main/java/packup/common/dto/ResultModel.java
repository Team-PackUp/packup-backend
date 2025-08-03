package packup.common.dto;

import lombok.Builder;
import lombok.Getter;
import packup.common.enums.ResponseMessage;

@Getter
@Builder
public class ResultModel<T> {
    private final String message;
    private final Boolean resultFlag;
    private final Integer statusCode;
    private final T response;

    public static <T> ResultModel<T> success(T data) {
        return ResultModel.<T>builder()
                .message(ResponseMessage.SUCCESS.message())
                .resultFlag(true)
                .statusCode(ResponseMessage.SUCCESS.httpStatus().value())
                .response(data)
                .build();
    }

    public static <T> ResultModel<T> fail(ResponseMessage responseMessage) {
        return ResultModel.<T>builder()
                .statusCode(responseMessage.httpStatus().value())
                .message(responseMessage.message())
                .resultFlag(false)
                .response(null)
                .build();
    }

    public static ResultModel<Void> success() {
        return ResultModel.<Void>builder()
                .message(ResponseMessage.SUCCESS.message())
                .resultFlag(true)
                .statusCode(ResponseMessage.SUCCESS.httpStatus().value())
                .response(null)
                .build();
    }

}
