package somt.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // === Member 관련 에러 ===
    USERNAME_NOT_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND,"member 을 찾을 수 없습니다."),

    // === JWT / Token 관련 에러 ===
    SERIALIZATION_FAIL(HttpStatus.BAD_REQUEST, "직렬화에 실패했습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_NOT_EFFECTIVE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    ACCESSTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 AccessToken 입니다."),
    NOT_ACCESSTOKEN(HttpStatus.UNAUTHORIZED, "AccessToken이 아닙니다."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "토큰이 일치하지 않습니다."),
    NOT_FOUND_REFRESHTOKEN(HttpStatus.UNAUTHORIZED, "RefreshToken 을 찾을 수 없습니다."),
    REFRESHTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED,"만료된 RefreshToken 입니다."),
    IS_NOT_REFRESHTOKEN(HttpStatus.UNAUTHORIZED,"RefreshToken 이 아닙니다."),

    // === 공통 / 잘못된 요청 에러 ===
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}