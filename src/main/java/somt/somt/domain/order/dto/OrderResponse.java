package somt.somt.domain.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;

    private BigDecimal totalPrice;

    private LocalDateTime createAt;

    private Long memberId;

    private List<OrderDetailDto> orderDetailDtoList =new ArrayList<>();


    public OrderResponse(Order order){
        this.orderId = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.createAt = order.getCreateAt();
        this.memberId=order.getMember().getId();
        this.orderDetailDtoList = order.getOrderDetailList().stream()
                .map(OrderDetailDto::new)
                .toList();
    }

}
