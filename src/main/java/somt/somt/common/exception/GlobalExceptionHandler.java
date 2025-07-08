package somt.somt.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ProblemDetail> validException(MethodArgumentNotValidException e){


        ProblemDetail problemDetail  = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("VALIDATION_FAILED");
        problemDetail.setDetail("입력값 검증에 실패 했습니다");
        problemDetail.setProperty("timestamp",LocalDateTime.now().toString());

        List<Map<String, Object>> errors = e.getFieldErrors().stream()
                .map(err -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("field",          err.getField());
                    m.put("rejectedValue",  err.getRejectedValue());  // null 허용
                    m.put("reason",         err.getDefaultMessage());
                    return m;
                })
                .toList();
        problemDetail.setProperty("errors",errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> ExceptionHandler(CustomException customException, HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatus(customException.getErrorCode().getHttpStatus());
        problemDetail.setTitle(customException.getErrorCode().name());
        problemDetail.setDetail(customException.getErrorCode().getMessage());
        problemDetail.setProperty("code",customException.getErrorCode().name());
        problemDetail.setProperty("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(customException.getErrorCode().getHttpStatus())
                .body(problemDetail);
    }



}
