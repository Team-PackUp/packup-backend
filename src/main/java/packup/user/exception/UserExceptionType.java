package packup.user.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import packup.common.exception.BaseExceptionType;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public enum UserExceptionType implements BaseExceptionType {


    NOT_FOUND_MEMBER(NOT_FOUND, "회원을 찾을 수 없습니다"),
    NOT_FOUND_USER_PREFER(NOT_FOUND, "선호도 정보가 없습니다"),
    NOT_FOUND_USER_DETAIL(NOT_FOUND, "상세 정보가 없습니다."),
    NOT_FOUND_NATION(NOT_FOUND, "국가를 찾을 수 없습니다."),
    NOT_FOUND_GENDER(NOT_FOUND, "성별을 찾을 수 없습니다."),
    ALREADY_WITHDRAW(NOT_ACCEPTABLE, "이미 탈퇴한 회원 입니다."),
    ALREADY_NICKNAME(NOT_ACCEPTABLE, "이미 존재하는 닉네임 입니다."),
    ABNORMAL_ACCESS(BAD_REQUEST, "비정상적인 접근입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
