package somt.somt.domain.product.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    private String productName;

    private BigDecimal price;

    private String img;

    private List<String> genres = new ArrayList<>();

}
