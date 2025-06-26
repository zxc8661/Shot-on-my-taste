package somt.somt.domain.cart.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartRequest;
import somt.somt.domain.cart.service.CartService;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/cart")
    public ResponseEntity<?> getCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
            )
    {
        return ResponseEntity.status(HttpStatus.FOUND).body("cart 탐색 성공");
    }

    @PostMapping("/user/cart")
    public ResponseEntity<?> createCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody CartRequest cartRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body("cart 생성 성공");
    }

    @PutMapping("/user/{cartId}")
    public ResponseEntity<?> modifyCartAmount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable(name = "cartId") Long cartId){
        return ResponseEntity.status(HttpStatus.OK).body("수량 변경 성공");
    }

    @DeleteMapping("/user/{cartId}")
    public ResponseEntity<?> deleteCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable(name = "cartId")Long cartId){
        return ResponseEntity.status(HttpStatus.OK).body("cart 삭제 성공");
    }

}
