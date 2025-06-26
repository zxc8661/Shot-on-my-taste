package somt.somt.domain.product.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    private String productName;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @NotBlank
    private String content;

    @NotEmpty
    private List<String> genres = new ArrayList<>();


}