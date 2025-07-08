package somt.somt.domain.product.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.image.ImageHandler;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genre.service.GenreService;
import somt.somt.domain.genreProduct.entity.GenreProduct;
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
    private final GenreProductService genreProductService;
    private final ProductThumbnailService productThumbnailService;




    public Map<String, Object> getProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.findAll(pageable);


        List<ProductDTO> productDTOS = productPage.stream()
                .map(p->{
                    List<String> genres = p.getGenreProductList().stream()
                            .map(gp->gp.getGenre().getName())
                            .collect(Collectors.toList());

                   return new ProductDTO(p.getId()
                           ,p.getProductName()
                           ,p.getPrice()
                           ,p.getProductThumbnails().get(0).getImagePath()
                           ,genres);
                })
                .collect(Collectors.toList());

        Map<String,Object> pageData = new HashMap<>();
        pageData.put("content",productDTOS);
        pageData.put("totalPageCount",productPage.getTotalPages());
        pageData.put("totalElementCount",productPage.getTotalElements());
        pageData.put("currentPage",productPage.getNumber());

        return pageData;

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
    public void modify(ProductRequest productRequest, Long id, List<MultipartFile> imageFiles) {

        Product product = productRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        product.modifyBasicInfo(
                productRequest.getProductName(),
                productRequest.getContent(),
                productRequest.getPrice(),
                productRequest.getStock()
        );

        productThumbnailService.modifyImageFile(imageFiles,product);
    }


    private List<String> setFilePath(List<MultipartFile> imageFiles){
        List<String> filePaths=new ArrayList<>();

        try {
            for (MultipartFile image : imageFiles) {
                if (!image.isEmpty()) {
                    String filePath = imageHandler.saveImage(image);
                    filePaths.add(filePath);
                }
            }

        }catch(IOException e){
            throw new CustomException(ErrorCode.BAD_FILEPATH);
        }

        return filePaths;
    }

    private void setGenreProduct(List<String> genres, Product product){
        for(String genreName : genres){
            Genre genre = genreService.getGere(genreName);
            GenreProduct genreProduct = genreProductService.create(genre,product);

            genre.getGenreProductList().add(genreProduct);
            product.getGenreProductList().add(genreProduct);
        }
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
