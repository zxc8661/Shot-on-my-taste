package somt.somt.domain.order.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.cart.service.CartService;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.order.dto.OrderResponse;
import somt.somt.domain.order.entity.Order;
import somt.somt.domain.order.entity.OrderDetail;
import somt.somt.domain.order.repository.OrderDetailRepository;
import somt.somt.domain.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            OrderDetail orderDetail = new OrderDetail(cart.getProduct(),order,cart.getAmount());

            BigDecimal price = cart.getProduct().getPrice();
            BigDecimal amount = new BigDecimal(cart.getAmount());
            totalPrice = totalPrice.add(price.multiply(amount));

            orderDetailRepository.save(orderDetail);

            cartService.deleteCart(customUserDetails,cart.getId());


        }
        order.modifyTotalPrice(totalPrice);

        return order.getId();
    }

    public CustomPageResponse<OrderResponse> getOrders(CustomUserDetails customUserDetails, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createAt").descending());

        Page<Order> orderPage = orderRepository.findByMemberId(customUserDetails.getMemberId(),pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(OrderResponse::new)
                .toList();

        return CustomPageResponse.of(
                orderResponses,
                orderPage.getTotalPages(),
                orderPage.getTotalElements(),
                orderPage.getNumber()
        );
    }


    public CustomPageResponse<OrderResponse> getAllOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createAt").descending());

        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(OrderResponse::new)
                .toList();


        return CustomPageResponse.of(
                orderResponses,
                orderPage.getTotalPages(),
                orderPage.getTotalElements(),
                orderPage.getNumber()
        );
    }
}
