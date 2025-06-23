package somt.somt.domain.comment.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final ProductService productService;
}
