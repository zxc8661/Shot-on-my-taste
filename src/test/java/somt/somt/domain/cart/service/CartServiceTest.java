package somt.somt.domain.cart.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartRequest;
import somt.somt.domain.cart.dto.CartResponse;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.cart.repository.CartRepository;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.repository.MemberRepository;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.repository.ProductRepository;
import somt.somt.domain.product.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;


@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private CartService cartService;



    Member sampleMember;
    Product sampleProduct;
    Cart sampleCart;

    CustomUserDetails sampleCustomUserDetails;

    @BeforeEach
    void setUp(){
        cartRepository.deleteAll();
        memberRepository.deleteAll();
        productRepository.deleteAll();

        sampleMember = Member.create(
                "테스트 이름",
                "테스트 패스워드",
                "테스트 닉네임",
                "테스트 이메일",
                "테스트 권한");

        sampleMember = memberRepository.save(sampleMember);

        sampleProduct = new Product(
                null,
                "테스트 상품",
                new BigDecimal("1000"),
                5,
                null, null, null, null, null,
                Collections.emptyList(),
                new ArrayList<>(),
                "내용",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        sampleProduct = productRepository.save(sampleProduct);

        sampleCart = new Cart(sampleMember,sampleProduct,10);

        sampleCustomUserDetails = new CustomUserDetails(
                new CustomUserData(
                        sampleMember.getId(),
                        sampleMember.getUserName(),
                        sampleMember.getRole(),
                        sampleMember.getPassword(),
                        sampleMember.getNickName()
                )
        );
    }



    @Test
    @DisplayName("cart 목록 조회 ")
    void getCarts() {
        //given
        cartRepository.save(sampleCart);

        //when
        List<CartResponse> cartResponseList = cartService.getCarts(sampleCustomUserDetails);

        //then
        assertEquals(1,cartResponseList.size());
        assertEquals(10,cartResponseList.get(0).getAmount());

    }

    @Test
    void create() {
        //given
        CartRequest cartRequest = new CartRequest(sampleProduct.getId(),20);
        cartService.create(sampleCustomUserDetails,cartRequest);

        //when
        List<CartResponse> cartResponseList = cartService.getCarts(sampleCustomUserDetails);

        //then
        assertEquals(1,cartResponseList.size());
        assertEquals(20,cartResponseList.get(0).getAmount());
        assertEquals(cartResponseList.get(0).getProductDTO().getProductName(),sampleProduct.getProductName());
    }



    @Test
    void modifyCartAmount() {
        //given

        Cart cart = cartRepository.save(sampleCart);
        cartService.modifyCartAmount(sampleCustomUserDetails,cart.getId(),50);

        //when
        List<CartResponse> cartResponseList = cartService.getCarts(sampleCustomUserDetails);

        //then
        assertEquals(1,cartResponseList.size());
        assertEquals(50,cartResponseList.get(0).getAmount());
        assertEquals(cartResponseList.get(0).getProductDTO().getProductName(),sampleProduct.getProductName());
    }

    @Test
    void modifyCartAmount_NotFound(){
        Long fakeId = 990L;
        CustomException ex = assertThrows(CustomException.class,
                ()-> cartService.modifyCartAmount(sampleCustomUserDetails,fakeId,10));
        assertEquals(ErrorCode.NOT_FOUND_CART,ex.getErrorCode());
    }


    @Test
    void deleteCart() {
        //given
        Cart cart = cartRepository.save(sampleCart);
        cartService.deleteCart(sampleCustomUserDetails, cart.getId());
        //when
        List<CartResponse> cartResponseList = cartService.getCarts(sampleCustomUserDetails);

        //then
        assertEquals(0,cartResponseList.size());
    }

    @Test
    void deleteCart_AccessDenied() {
        Cart saved = cartRepository.save(sampleCart);
        CustomUserDetails otherUser = new CustomUserDetails(
                new CustomUserData(saved.getMember().getId()+1, "...", "ROLE_USER", "...", "...")
        );
        CustomException ex = assertThrows(CustomException.class,
                () -> cartService.deleteCart(otherUser, saved.getId()));
        assertEquals(ErrorCode.CART_ACCESS_DENIED, ex.getErrorCode());
    }
}