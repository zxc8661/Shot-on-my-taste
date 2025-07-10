package somt.somt.domain.order.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.cart.service.CartService;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.order.entity.Order;
import somt.somt.domain.order.entity.OrderDetail;
import somt.somt.domain.order.repository.OrderDetailRepository;
import somt.somt.domain.order.repository.OrderRepository;
import somt.somt.domain.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final CartService cartService;
    private final MemberService memberService;

    @Transactional
    public Long create(CustomUserDetails customUserDetails) {
        List<Cart> cartList = cartService.getCarts(customUserDetails.getMemberId());
        Member member =  memberService.getMember(customUserDetails.getMemberId());

        Order order = new Order(member);

        orderRepository.save(order);

        BigDecimal totalPrice = new BigDecimal(0);

        for(Cart cart : cartList){
            OrderDetail orderDetail = new OrderDetail(cart.getProduct(),order);

            BigDecimal price = cart.getProduct().getPrice();
            BigDecimal amount = new BigDecimal(cart.getAmount());
            totalPrice = totalPrice.add(price.multiply(amount));

            orderDetailRepository.save(orderDetail);

            cartService.deleteCart(customUserDetails,cart.getId());


        }
        order.modifyTotalPrice(totalPrice);

        return order.getId();
    }
}
