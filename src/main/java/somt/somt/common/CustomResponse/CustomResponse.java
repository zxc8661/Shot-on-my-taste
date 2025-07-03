package somt.somt.common.CustomResponse;


import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String timestamp;

    public CustomResponse(boolean success, String message,T data){
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }
}
