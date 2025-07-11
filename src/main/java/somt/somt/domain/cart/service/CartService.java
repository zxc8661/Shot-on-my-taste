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

    public List<CartResponse> getCartResponse(CustomUserDetails userDetails) {
        List<Cart> carts = getCarts(userDetails.getMemberId());

        List<CartResponse> cartResponses = carts.stream()
                .map(CartResponse::new)
                .toList();
        return cartResponses;
    }

    @Transactional
    public Long create(CustomUserDetails userDetails, CartRequest cartRequest) {
        Member member = memberService.getMember(userDetails.getMemberId());
        Product product = productService.getProduct(cartRequest.getProductId());

        Cart cart;

        if(cartRepository.existsByMemberIdAndProductId(member.getId(),product.getId()))
        {
            cart = cartRepository.findByMemberIdAndProductId(member.getId(),product.getId());
            cart.amountModify(cart.getAmount()+cartRequest.getAmount());
        }else{
            Cart newCart = new Cart(member,product,cartRequest.getAmount());
            cart = cartRepository.save(newCart);
        }

        cart =cartRepository.save(cart);
        return cart.getId();
    }


    @Transactional
    public Long modifyCartAmount(CustomUserDetails userDetails, Long cartId, Integer amount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CART));

       authorityCheck(userDetails.getMemberId(),cart.getMember().getId());

        cart.amountModify(amount);

        return cart.getId();
    }

    @Transactional
    public void deleteCart(CustomUserDetails userDetails, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_CART));

        authorityCheck(userDetails.getMemberId(),cart.getMember().getId());

        cartRepository.delete(cart);
    }

    @Transactional
    public void deleteAll(CustomUserDetails customUserDetails) {
        cartRepository.deleteAllByMemberId(customUserDetails.getMemberId());

    }

    public List<Cart> getCarts(Long memberId){
       return cartRepository.findAllByMemberId(memberId);
    }




    private void authorityCheck(Long memberId,Long cartMemberId){
        if(!Objects.equals(memberId, cartMemberId)){
            throw new CustomException(ErrorCode.CART_ACCESS_DENIED);
        }

    }



}
