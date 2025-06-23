package somt.somt.domain.product.cotroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import somt.somt.domain.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping("")
    public ResponseEntity<?> getProducts(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "50") int size){
        return ResponseEntity.ok("굿");
    }


    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(
            @PathVariable(name = "productId") Long productId){
        return ResponseEntity.ok("good");
    }

//    @PostMapping
//    public ResponseEntity<?> create(
//            @RequestBody
//    )

}
