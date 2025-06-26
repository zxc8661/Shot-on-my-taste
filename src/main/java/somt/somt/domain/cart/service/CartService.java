package somt.somt.domain.cart.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartRequest;
import somt.somt.domain.cart.dto.CartResponse;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.cart.repository.CartRepository;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.member.service.MemberService;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.service.ProductService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final MemberService memberService;

    public List<CartResponse> getCarts(CustomUserDetails userDetails) {
        List<Cart> carts = cartRepository.findAllByMemberId(userDetails.getMemberId());

        List<CartResponse> cartResponses = carts.stream()
                .map(CartResponse::new)
                .toList();
        return cartResponses;
    }

    @Transactional
    public void create(CustomUserDetails userDetails, CartRequest cartRequest) {
        Member member = memberService.getMember(userDetails.getMemberId());
        Product product = productService.getProduct(cartRequest.getProductId());

        Cart cart = new Cart(member,product,cartRequest.getAmount());

        cartRepository.save(cart);
    }


    public void modifyCartAmount(CustomUserDetails userDetails, Long cartId, Integer amount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CART));

       authorityCheck(userDetails.getMemberId(),cart.getMember().getId());

        cart.amountModify(amount);

        cartRepository.save(cart);
    }

    public void deleteCart(CustomUserDetails userDetails, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CART));

        authorityCheck(userDetails.getMemberId(),cart.getMember().getId());

        cartRepository.delete(cart);
    }


    private void authorityCheck(Long memberId,Long cartMemberId){
        if(memberId!=cartMemberId){
            throw new CustomException(ErrorCode.CART_ACCESS_DENIED);
        }

    }
}
