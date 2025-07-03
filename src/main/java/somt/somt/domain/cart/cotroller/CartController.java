package somt.somt.domain.cart.cotroller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartRequest;
import somt.somt.domain.cart.dto.CartResponse;
import somt.somt.domain.cart.service.CartService;
import somt.somt.domain.product.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/cart")
    public ResponseEntity<?> getCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
            )
    {
        List<CartResponse> cartList = cartService.getCarts(userDetails);

        return ResponseEntity.status(HttpStatus.FOUND).body(cartList);
    }

    @PostMapping("/user/cart")
    public ResponseEntity<?> createCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody @Valid CartRequest cartRequest){

        cartService.create(userDetails,cartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("cart 생성 성공");
    }

    @PutMapping("/user/cart/{cartId}")
    public ResponseEntity<?> modifyCartAmount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable(name = "cartId") Long cartId,
                                              @RequestParam(name = "amount") Integer amount){

        cartService.modifyCartAmount(userDetails,cartId,amount);
        return ResponseEntity.status(HttpStatus.OK).body("수량 변경 성공");
    }

    @DeleteMapping("/user/{cartId}")
    public ResponseEntity<?> deleteCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable(name = "cartId")Long cartId){
        cartService.deleteCart(userDetails,cartId);
        return ResponseEntity.status(HttpStatus.OK).body("cart 삭제 성공");
    }

}
