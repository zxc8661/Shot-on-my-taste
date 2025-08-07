package somt.somt.domain.cart.cotroller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.cart.dto.CartRequest;
import somt.somt.domain.cart.dto.CartResponse;
import somt.somt.domain.cart.service.CartService;
import somt.somt.domain.product.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "장바구니 API")
public class CartController {

    private final CartService cartService;

    @PostMapping("/user/cart")
    @Operation(summary = "장바구니 생성",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201",description="장바구니 생성 성공",content = @Content(schema = @Schema(implementation = CartIdResponse.class)))
    public ResponseEntity<?> createCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody @Valid CartRequest cartRequest){

        Long cartId =cartService.create(userDetails,cartRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success(cartId,"장바구니 생성 성공"));
    }

    @GetMapping("/user/cart")
    @Operation(summary = "장바구니 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "장바구니 조회 성공", content = @Content(schema = @Schema(implementation = CartListResponse.class)))
    public ResponseEntity<?> getCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        List<CartResponse> cartList = cartService.getCartResponse(userDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(cartList,"장바구니 목록 조회 성공"));
    }



    @PutMapping("/user/cart/{cartId}")
    @Operation(summary = "장바구니 양 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "장바구니 수정 성공",content = @Content(schema = @Schema(implementation = CartIdResponse.class)))
    public ResponseEntity<?> modifyCartAmount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable(name = "cartId") Long cartId,
                                              @RequestParam(name = "amount") Integer amount){

        Long response = cartService.modifyCartAmount(userDetails,cartId,amount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"상품 수정 성공"));
    }

    @DeleteMapping("/user/cart/{cartId}")
    @Operation(summary = "장바구니 삭제",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "장바구니 삭제 성공",content = @Content(schema = @Schema(implementation = CartStringResponse.class)))
    public ResponseEntity<?> deleteCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable(name = "cartId")Long cartId){
        cartService.deleteCart(userDetails,cartId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("카트 삭제 성공"));
    }

    @DeleteMapping("/user/cart/deleteAll")
    @Operation(summary = " 장바구니 일괄 삭제 ",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "장바구니 일괄사겢 성공",content = @Content(schema =@Schema(implementation = CartStringResponse.class)))
    public ResponseEntity<?> deleteAllCart(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        cartService.deleteAll(customUserDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("모든 카트 삭제 성공"));

    }

}


@Schema(description = "장바구니 응답 Id")
class CartIdResponse extends CustomResponse<Long>{};

@Schema(description = "장바구니 응답 String")
class CartStringResponse extends CustomResponse<String>{};


@Schema(description = "장바구니 목록")
class CartListResponse extends CustomResponse<List<CartResponse>>{}
