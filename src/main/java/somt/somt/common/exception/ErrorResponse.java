package somt.somt.common.exception;


import lombok.Getter;

import java.time.LocalDateTime;

// 참고 포스팅 : https://ngwdeveloper.tistory.com/156
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String error;
    private final String message;

    public ErrorResponse(ErrorCode errorCode){
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(int code, String error, String message){
        this.statusCode = code;
        this.error = error;
        this.message = message;
    }

}
