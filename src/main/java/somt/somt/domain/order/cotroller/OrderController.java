package somt.somt.domain.order.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.order.service.OrderService;
import somt.somt.domain.product.service.ProductService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/user/orders")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        Long response = orderService.create(customUserDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CustomResponse<>(true,"주문 생성 성공",response));
    }

    @GetMapping("/user/orders")
    public ResponseEntity<?> getOrders(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "30") Integer size
    ){
        Map<String,Object> response = orderService.getOrders(customUserDetails,page,size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse<>(true,"주문 목록 조회 성공",response));

    }

    @GetMapping("/admin/orders")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "30") Integer size
    ){
        Map<String,Object> response = orderService.getAllOrders(page,size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse<>(true,"주문 목록 조회 성공",response));

    }
}
