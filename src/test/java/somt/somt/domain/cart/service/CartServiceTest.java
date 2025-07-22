package somt.somt.domain.cart.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartRequest;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.cart.repository.CartRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;
    @Mock
    ProductService productService;
    @Mock
    MemberService memberService;
    @InjectMocks
    CartService cartService;

    @Test
    public void create_true(){
        //given
        CustomUserDetails mockCustomUserDetails =
                new CustomUserDetails(
                        new CustomUserData(
                                1L,
                                "member",
                                "ADMIN",
                                "",
                                "nickname")
                );
        CartRequest cartRequest  = new CartRequest(10L,10);
        Member mockMember = new Member(mockCustomUserDetails);
        Product mockProduct = new Product(10L,"mock",new BigDecimal("100"),10,"10");

        Cart mockCart =new Cart(10L,mockMember,mockProduct,10, LocalDateTime.now());



        when(productService.getProduct(anyLong())).thenReturn(mockProduct);
        when(memberService.getMember(anyLong())).thenReturn(mockMember);
        when(cartRepository.existsByMemberIdAndProductId(mockMember.getId(),mockProduct.getId())).thenReturn(true);
        when(cartRepository.findByMemberIdAndProductId(mockMember.getId(),mockProduct.getId())).thenReturn(mockCart);

        //when
        Long result = cartService.create(mockCustomUserDetails,cartRequest);
        //then
        verify(cartRepository).existsByMemberIdAndProductId(mockMember.getId(),mockProduct.getId());
        verify(cartRepository).findByMemberIdAndProductId(mockMember.getId(),mockProduct.getId());

    }

    @Test
    public void create_false(){
        //given
        CustomUserDetails mockCustomUserDetails =
                new CustomUserDetails(
                        new CustomUserData(
                                1L,
                                "member",
                                "ADMIN",
                                "",
                                "nickname")
                );
        CartRequest cartRequest  = new CartRequest(10L,10);
        Member mockMember = new Member(mockCustomUserDetails);
        Product mockProduct = new Product(10L,"mock",new BigDecimal("100"),10,"10");

        Cart mockCart =new Cart(10L,mockMember,mockProduct,10, LocalDateTime.now());



        when(productService.getProduct(anyLong())).thenReturn(mockProduct);
        when(memberService.getMember(anyLong())).thenReturn(mockMember);
        when(cartRepository.existsByMemberIdAndProductId(mockMember.getId(),mockProduct.getId())).thenReturn(false);
        when(cartRepository.save(any())).thenReturn(mockCart);
        //when
        Long result = cartService.create(mockCustomUserDetails,cartRequest);



        //then
        assertThat(result).isEqualTo(10L);
        verify(cartRepository).save(any());


    }




}