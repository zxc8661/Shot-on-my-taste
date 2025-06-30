package somt.somt.domain.comment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotNull
    private Long productId;

    @NotBlank
    private String content;

    @NotNull
    private Integer grade;

    private Long parentId;
}
