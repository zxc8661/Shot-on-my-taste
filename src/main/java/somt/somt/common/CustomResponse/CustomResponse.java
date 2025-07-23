package somt.somt.common.CustomResponse;


import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
    private boolean success;
    private T content;
    private String message;
    private String timestamp;


    public static <T> CustomResponse<T> success(T data){
        return new CustomResponse<>(
                true,
                data,
                "요청이 성공적으로 처리되었습니다.",
                LocalDateTime.now().toString()
        );
    }

    public static <T> CustomResponse<T> success(T data,String message){
        return new CustomResponse<>(
                true,
                data,
                message,
                LocalDateTime.now().toString()
        );
    }

    public static <T> CustomResponse<T> success(String message){
        return new CustomResponse<>(
                true,
                null,
                message,
                LocalDateTime.now().toString()
        );
    }

    public static <T> CustomResponse<T> fail(String message){
        return new CustomResponse<>(
                false,
                null,
                message,
                LocalDateTime.now().toString()
        );
    }
}
