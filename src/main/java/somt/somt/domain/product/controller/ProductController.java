package somt.somt.domain.product.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.product.dto.reponse.ProductDetailDTO;
import somt.somt.domain.product.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    @GetMapping("/public/products")
    public ResponseEntity<?> getProducts(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "30") int size){


        Map<String,Object> response =productService.getProducts(page,size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping("/public/products/{productId}")
    public ResponseEntity<?> getProductDetails(
            @PathVariable(name = "productId") Long productId){
        ProductDetailDTO productDetailDTO = productService.getProductDetails(productId);

        return ResponseEntity.status(HttpStatus.OK).body(productDetailDTO);
    }


    @PostMapping("/admin/products")
    public ResponseEntity<?> create(
            @ModelAttribute @Valid ProductRequest dto,
            @RequestParam("imageFiles") List<MultipartFile> files
    ) throws IOException {

        productService.create(dto, files);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto.getContent());
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<?> modify(
            @RequestBody @Valid ProductRequest productModify,
            @PathVariable(name = "productId") Long id,
            @RequestPart List<MultipartFile> imageFiles
    ){
        productService.modify(productModify,id,imageFiles);

        return ResponseEntity.status(HttpStatus.OK).body("상품 수정 성공");
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<?> delete(@PathVariable(name = "productId") Long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("상품 삭제 성공");
    }

}
