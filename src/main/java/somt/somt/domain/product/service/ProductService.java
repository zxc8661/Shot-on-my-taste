package somt.somt.domain.product.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.CustomResponse.CustomPageResponse;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.image.ImageHandler;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genre.service.GenreService;
import somt.somt.domain.genreProduct.service.GenreProductService;
import somt.somt.domain.product.dto.reponse.ProductDTO;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.product.dto.reponse.ProductDetailDTO;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.repository.ProductRepository;
import somt.somt.domain.productThumbnail.service.ProductThumbnailService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageHandler imageHandler;
    private final GenreService genreService;
    private final ProductThumbnailService productThumbnailService;





    public CustomPageResponse<ProductDTO> getProductSearch(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("create_at").descending());

        Page<Product> productPage ;

        if(keyword.isEmpty()){
           productPage    = productRepository.findAll(pageable);
        }else{
            productPage = productRepository.searchByKeyword(keyword,pageable);
        }

        List<ProductDTO> productDTOS = productPage.stream()
                .map(ProductDTO ::new)
                .collect(Collectors.toList());


        return  CustomPageResponse.of(
                productDTOS,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getNumber()
        );
    }

    public ProductDetailDTO getProductDetails(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        ProductDetailDTO productDetailDTO = ProductDetailDTO.toDTO(product);

        return productDetailDTO;
    }

    @Transactional
    public ProductDetailDTO create(ProductRequest productRequest, List<MultipartFile> imageFiles) {

        if(productRepository.existsByProductName(productRequest.getProductName())){
            throw new CustomException(ErrorCode.DUPLICATE_PRODUCTNAME);
        }


        Product product = Product.create(productRequest);

        Product saveProduct = productRepository.save(product);


        for(String genre : productRequest.getGenres()){
            Genre tmpGenre = genreService.getGere(genre);
            saveProduct.addGenreProduct(tmpGenre);
        }


        productThumbnailService.uploadImageFile(imageFiles,saveProduct);

        return ProductDetailDTO.toDTO(saveProduct);
    }

    @Transactional
    public Long modify(ProductRequest productRequest, Long id, List<MultipartFile> imageFiles) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        product.getGenreProductList().clear();
        product.getProductThumbnails().clear();

        for (String strGenre : productRequest.getGenres()) {
            Genre genre = genreService.getGere(strGenre);

            product.addGenreProduct(genre);
        }

        product.modifyBasicInfo(
                productRequest.getProductName(),
                productRequest.getContent(),
                productRequest.getPrice(),
                productRequest.getStock()
        );

        productThumbnailService.modifyImageFile(imageFiles, product);

        return product.getId();
    }


    public void delete(Long id) {
        Product product =productRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        productRepository.delete(product);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }




}
