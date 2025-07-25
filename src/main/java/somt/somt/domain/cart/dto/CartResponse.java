package somt.somt.domain.cart.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.product.dto.reponse.ProductDTO;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.productThumbnail.entity.ProductThumbnail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long cartId;
    private Integer amount;
    private ProductDTO productDTO;
    private LocalDateTime createAt;


    public CartResponse(Cart cart){
        Product product = cart.getProduct();

        this.cartId=cart.getId();
        this.amount = cart.getAmount();
        this.productDTO = new ProductDTO(product);
        this.createAt = cart.getCreateAt();
    }

}
