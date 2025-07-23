package somt.somt.domain.product.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.product.dto.reponse.ProductDTO;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.product.dto.reponse.ProductDetailDTO;
import somt.somt.domain.product.service.ProductService;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/admin/products")
    public ResponseEntity<?> create(
            @ModelAttribute @Valid ProductRequest dto,
            @RequestParam(value = "imageFiles") List<MultipartFile> files
    ) throws IOException {

        ProductDetailDTO productDetailDTO = productService.create(dto, files);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success(productDetailDTO,"상품 추가 성공 "));
    }




    @GetMapping("/public/products/{productId}")
    public ResponseEntity<?> getProductDetails(
            @PathVariable(name = "productId") Long productId){
        ProductDetailDTO productDetailDTO = productService.getProductDetails(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(productDetailDTO,"상품 상세 조회 성공 "));
    }

    @GetMapping("/public/products/search")
    public ResponseEntity<?> gerProductSearch(
            @RequestParam(name = "keyword",defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "30") int size
    ){
        Map<String,Object> response = productService.getProductSearch(keyword,page,size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"삼품 검색 성공"));

    }



    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<?> modify(
            @ModelAttribute @Valid ProductRequest productRequest,
            @PathVariable(name = "productId") Long id,
            @RequestParam(name = "imageFiles") List<MultipartFile> imageFiles
    ){
       Long productId= productService.modify(productRequest,id,imageFiles);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(productId,"상품 수정 성공"));
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<?> delete(@PathVariable(name = "productId") Long id){
        productService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("상품 삭제 성공 "));
    }

}
