package somt.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found user"),
    USERNAME_NOT_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호가 일치하지 않습니다."),
    SERIALIZATION_FAIL(HttpStatus.BAD_REQUEST, "직렬화에 실패 했습니다."),
    NOT_FUND_COOKIE(HttpStatus.UNAUTHORIZED, "SESSIONID 쿠키를 찾을 수 없습니다."),
    SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "SESSION을 찾을 수 없습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST,"Invalid request.");

    private final HttpStatus httpStatus;
    private final String message;

    }
