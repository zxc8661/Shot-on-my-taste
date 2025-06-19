package somt.somt.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> customException(CustomException ce){
        ErrorResponse errorResponse = new ErrorResponse(ce.getErrorCode());
        return ResponseEntity
                .status(ce.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException e){
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error->error.getDefaultMessage())
                .orElse("잘못된 요청입니다.");

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.BAD_REQUEST.getHttpStatus().value(),
                ErrorCode.BAD_REQUEST.getHttpStatus().name(),
                errorMessage
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
