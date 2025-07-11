package somt.somt.domain.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.order.entity.OrderDetail;
import somt.somt.domain.product.dto.reponse.ProductDTO;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long id;

    private String productName;

    private BigDecimal price;

    private Integer amount;


    public OrderDetailDto(OrderDetail orderDetail){
        this.id = orderDetail.getId();
        this.productName = orderDetail.getProduct().getProductName();
        this.price = orderDetail.getProduct().getPrice();
        this.amount =orderDetail.getAmount();
    }
}
