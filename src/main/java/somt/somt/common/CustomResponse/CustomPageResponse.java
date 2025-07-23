package somt.somt.common.CustomResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageResponse<T> {
    private List<T> content;
    private Integer totalPageCount;
    private Long totalElementCount;
    private Integer currentPage;

    public static <T> CustomPageResponse<T> of(List<T> content,
                                               Integer totalPageCount,
                                               Long totalElementCount,
                                               Integer currentPage){
        return new CustomPageResponse<>(
                content,
                totalPageCount,
                totalElementCount,
                currentPage
        );
    }

}
