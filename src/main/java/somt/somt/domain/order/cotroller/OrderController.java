package somt.somt.domain.order.cotroller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.order.dto.OrderResponse;
import somt.somt.domain.order.service.OrderService;
import somt.somt.domain.product.service.ProductService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "주문 API")
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/user/orders")
    @Operation(summary = "주문 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(schema = @Schema(implementation = OrderIdResponse.class)))
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        Long response = orderService.create(customUserDetails);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success(response,"주문 생성 성공 "));
    }

    @GetMapping("/user/orders")
    @Operation(summary = "주문 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "주문 목록 조회 성공(회원)", content = @Content(schema = @Schema(implementation = OrderPageResponse.class)))
    public ResponseEntity<?> getOrders(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "30") Integer size
    ){
        CustomPageResponse<OrderResponse> response = orderService.getOrders(customUserDetails,page,size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"주문 목록 조회 성공 "));
    }

    @GetMapping("/admin/orders")
    @Operation(summary = "주문 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200",description = "주문 목록 조회 성공(관리자)", content = @Content(schema = @Schema(implementation = OrderPageResponse.class)))
    public ResponseEntity<?> getAllOrders(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "30") Integer size
    ){
        CustomPageResponse<OrderResponse> response = orderService.getAllOrders(page,size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"주문 목록 조회 성공 "));

    }
}

@Schema(name = "주문 응답 Id")
class OrderIdResponse extends CustomResponse<Long>{};

@Schema(name = "주문 응답 page")
class OrderPageResponse extends CustomResponse<CustomPageResponse<OrderResponse>>{};