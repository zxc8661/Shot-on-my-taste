package somt.somt.domain.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.CustomResponse.CustomResponse;
import somt.somt.common.exception.ErrorResponse;

import somt.somt.common.swagger.product.ProductDetailResponse;
import somt.somt.common.swagger.product.ProductIdResponse;
import somt.somt.common.swagger.product.ProductSearchListResponse;
import somt.somt.domain.product.dto.reponse.ProductDTO;
import somt.somt.domain.product.dto.reponse.ProductDetailDTO;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.product.service.ProductService;

import java.io.IOException;
import java.util.List;



@Tag(name ="Product API",description = "상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", security=@SecurityRequirement(name= "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 추가 성공", content = @Content(schema = @Schema(implementation = ProductDetailResponse.class))),
            @ApiResponse(responseCode = "400",description = "잘못된 요청 값" , content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/admin/products")
    public ResponseEntity<CustomResponse<ProductDetailDTO>> create(
            @ModelAttribute @Valid ProductRequest dto,
            @RequestParam(value = "imageFiles") List<MultipartFile> files
    ) throws IOException {

        ProductDetailDTO productDetailDTO = productService.create(dto, files);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.success(productDetailDTO,"상품 추가 성공 "));
    }



    @Operation(summary = "상품 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "상품 상세 정보 조회 성공",content = @Content(schema = @Schema(implementation = ProductDetailResponse.class))),
            @ApiResponse(responseCode ="404",description = "해당 ID의 상품을 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/public/products/{productId}")
    public ResponseEntity<CustomResponse<ProductDetailDTO>> getProductDetails(
            @PathVariable(name = "productId") Long productId){
        ProductDetailDTO productDetailDTO = productService.getProductDetails(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(productDetailDTO,"상품 상세 조회 성공 "));
    }

    @Operation(summary = "상품 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode= "200",description = "상품 검색 성공",content = @Content(schema = @Schema(implementation = ProductSearchListResponse.class)))
    })

    @GetMapping("/public/products/search")
    public ResponseEntity<CustomResponse<CustomPageResponse<ProductDTO>>> getProductSearch(
            @RequestParam(name = "keyword",defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "30") int size
    ){
        CustomPageResponse<ProductDTO> response = productService.getProductSearch(keyword,page,size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success(response,"삼품 검색 성공"));

    }



    @Operation(summary = "상품 수정",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode="200", description = "상품 수정 성공", content = @Content(schema = @Schema(implementation = ProductIdResponse.class)))
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


    @Operation(summary = "상품 삭제" , security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(schema = @Schema(implementation = ProductIdResponse.class)))
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<?> delete(@PathVariable(name = "productId") Long id){
        productService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomResponse.success("상품 삭제 성공 "));
    }

}








