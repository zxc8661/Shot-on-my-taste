package somt.somt.domain.order.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final ProductService productService;
}
