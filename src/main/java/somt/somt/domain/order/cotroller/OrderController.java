package somt.somt.domain.order.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.order.service.OrderService;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/orders")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        Long response = orderService.create(customUserDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CustomResponse<>(true,"주문 생성 성공",response));
    }
}
