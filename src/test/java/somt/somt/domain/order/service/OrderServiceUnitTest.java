//package somt.somt.domain.order.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import somt.somt.common.security.dto.CustomUserData;
//import somt.somt.common.security.dto.CustomUserDetails;
//import somt.somt.domain.cart.entity.Cart;
//import somt.somt.domain.cart.service.CartService;
//import somt.somt.domain.member.entity.Member;
//import somt.somt.domain.member.service.MemberService;
//import somt.somt.domain.order.entity.Order;
//import somt.somt.domain.order.repository.OrderDetailRepository;
//import somt.somt.domain.order.repository.OrderRepository;
//import somt.somt.domain.product.entity.Product;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceUnitTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private OrderDetailRepository orderDetailRepository;
//    @Mock
//    private CartService cartService;
//    @Mock
//    private MemberService memberService;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @Test
//    void create() {
//        // given
//        CustomUserDetails userDetails = new CustomUserDetails(new CustomUserData(1L, "testuser", "USER", "", "testnick"));
//        Member member = new Member();
//        Product product = new Product(1L, "Test Product", new BigDecimal("10.00"), 100, "Test Description");
//        Cart cart = new Cart(1L,member,product,10, LocalDateTime.now());
//        List<Cart> cartList = Collections.singletonList(cart);
//        Order order = new Order(member);
//
//        when(cartService.getCarts(anyLong())).thenReturn(cartList);
//        when(memberService.getMember(anyLong())).thenReturn(member);
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        // when
//        Long orderId = orderService.create(userDetails);
//
//        // then
//        assertThat(orderId).isEqualTo(order.getId());
//        verify(orderDetailRepository).save(any());
//        verify(cartService).deleteCart(any(), anyLong());
//    }
//
//    @Test
//    void getOrders() {
//        // given
//        CustomUserDetails userDetails = new CustomUserDetails(new CustomUserData(1L, "testuser", "USER", "", "testnick"));
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());
//        Order order = new Order(new Member());
//        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order), pageable, 1);
//
//        when(orderRepository.findByMemberId(anyLong(), any(Pageable.class))).thenReturn(orderPage);
//
//        // when
//        Map<String, Object> result = orderService.getOrders(userDetails, 0, 10);
//
//        // then
//        assertThat(result.get("totalPages")).isEqualTo(1);
//        assertThat(result.get("currentPage")).isEqualTo(0);
//        assertThat(result.get("totalElements")).isEqualTo(1L);
//    }
//
//    @Test
//    void getAllOrders() {
//        // given
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());
//        Order order = new Order(new Member());
//        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order), pageable, 1);
//
//        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);
//
//        // when
//        Map<String, Object> result = orderService.getAllOrders(0, 10);
//
//        // then
//        assertThat(result.get("totalPages")).isEqualTo(1);
//        assertThat(result.get("currentPage")).isEqualTo(0);
//        assertThat(result.get("totalElements")).isEqualTo(1L);
//    }
//}
