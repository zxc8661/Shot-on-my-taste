package somt.somt.domain.genre.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final ProductService productService;
}
