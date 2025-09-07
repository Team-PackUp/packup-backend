package packup.tour.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum TourInfoExceptionType implements BaseExceptionType {

    INVALID_TIME_RANGE(BAD_REQUEST, "세션 종료시간은 시작시간 이후여야 합니다."),
    MISMATCH_TOUR_SEQ(BAD_REQUEST, "요청의 tourSeq가 경로와 일치하지 않습니다."),
    NOT_FOUND_TOUR(NOT_FOUND, "해당 투어를 찾을 수 없습니다."),
    FORBIDDEN_TOUR_ACCESS(FORBIDDEN, "해당 투어에 대한 권한이 없습니다."),
    SESSION_OVERLAPPED(CONFLICT, "해당 시간대에 이미 세션이 존재합니다."),
    NOT_FOUND_SESSION(NOT_FOUND, "해당 세션을 찾을 수 없습니다."),
    FAIL_TO_SAVE_SESSION(INTERNAL_SERVER_ERROR, "세션 저장에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override public HttpStatus httpStatus() { return httpStatus; }
    @Override public String errorMessage() { return errorMessage; }
}
