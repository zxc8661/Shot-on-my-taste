package somt.somt.common.exception;

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
    USERNAME_NOT_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "Username and password do not match."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "Member not found."),
    NOT_MATCH_PASSWORDS(HttpStatus.BAD_REQUEST, "Passwords do not match."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,"Username is duplicate."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"NickName is duplicate."),
    BAD_REGISTER_REQUEST(HttpStatus.BAD_REQUEST,"Bad register request"),

    // === Genre 관련 에러 ===
    GENRE_EXIST(HttpStatus.BAD_REQUEST,"This genre exist"),
    NOT_FOUND_GENRE(HttpStatus.NOT_FOUND,"Genre not found"),


    // === JWT / Token 관련 에러 ===
    SERIALIZATION_FAIL(HttpStatus.BAD_REQUEST, "Serialization failed."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired."),
    TOKEN_NOT_EFFECTIVE(HttpStatus.UNAUTHORIZED, "Token is not valid."),
    ACCESSTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "Access token has expired."),
    NOT_ACCESSTOKEN(HttpStatus.UNAUTHORIZED, "Not an access token."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "Token mismatch."),
    NOT_FOUND_REFRESHTOKEN(HttpStatus.UNAUTHORIZED, "Refresh token not found."),
    REFRESHTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired."),
    IS_NOT_REFRESHTOKEN(HttpStatus.UNAUTHORIZED, "Not a refresh token."),
    WITHDRAWAL_MEMBER(HttpStatus.BAD_REQUEST,"This member is withdrawal"),
    NOT_FOUND_ACCESSTOKEN(HttpStatus.NOT_FOUND,"AccessToken not found"),
    NOT_ADMIN(HttpStatus.UNAUTHORIZED,"You are not admin"),

    // === 공통 / 잘못된 요청 에러 ===
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request.");

    private final HttpStatus httpStatus;
    private final String message;
}