package somt.somt.domain.notification.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final ProductService productService;
}
