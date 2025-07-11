package somt.somt.common.CustomResponse;


import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CustomResponse<T> {
    private boolean success;
    private Map<String,Object> data = new HashMap<>();
    private String message;
    private String timestamp;

    public CustomResponse(boolean success, String message,String dataName,Object data){
        this.success = success;
        this.message = message;

        this.timestamp = LocalDateTime.now().toString();

        Map<String,Object> tmp = new HashMap<>();
        tmp.put(dataName,data);
        this.data = tmp;
    }
}
