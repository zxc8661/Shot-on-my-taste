package somt.somt.domain.cart.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;
}
