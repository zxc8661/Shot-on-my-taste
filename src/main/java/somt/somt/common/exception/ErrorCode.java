package somt.somt.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import somt.somt.domain.productThumbnail.entity.ProductThumbnail;
import somt.somt.domain.productThumbnail.service.ProductThumbnailService;

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
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST,"This email invalid" ),

    // === Genre 관련 에러 ===
    GENRE_EXIST(HttpStatus.BAD_REQUEST,"This genre exist"),
    NOT_FOUND_GENRE(HttpStatus.NOT_FOUND,"Genre not found"),

    // === Product 관련 에러 ===
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND,"Product not found"),
    DUPLICATE_PRODUCTNAME(HttpStatus.BAD_REQUEST,"ProductName is duplicate." ),

    // === ProductThumbnail 관련 에러 ===
    IMAGE_FILE_EMPTY(HttpStatus.BAD_REQUEST,"Image file empty."),
    IMAGE_FILE_SAVE_FAIL(HttpStatus.BAD_REQUEST, "Image cant save"),
    IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST,"Cant found fail" ),
    // === Cart 관련 에러 ===
    NOT_FOUND_CART(HttpStatus.NOT_FOUND,"Cart not found" ),
    CART_ACCESS_DENIED(HttpStatus.UNAUTHORIZED,"You are not authorized to access this cart" ),

    // === Address 관련 에러 ===
    NOT_FOUND_ADDRESS(HttpStatus.NOT_FOUND,"Address not found"),

    // === Comment 관련 에러 ===
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND,"Comment ot found"),

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
    BAD_FILEPATH(HttpStatus.BAD_REQUEST,"Bad filePath"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED,"You can't access because  your member ID does not match.");



    private final HttpStatus httpStatus;
    private final String message;
}