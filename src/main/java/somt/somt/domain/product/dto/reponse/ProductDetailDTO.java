package somt.somt.domain.product.dto.reponse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.productThumbnail.entity.ProductThumbnail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private Long id;
    private String productName;
    private Integer stock;
    private List<String> images= new ArrayList<>();
    private String content;
    private List<String> genres=new ArrayList<>();
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;


    public static ProductDetailDTO toDTO(Product product) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.id= product.getId();
        productDetailDTO.productName=product.getProductName();
        productDetailDTO.stock = product.getStock();
        productDetailDTO.images=product.getProductThumbnails().stream()
                .map(ProductThumbnail::getImagePath)
                .toList();
        productDetailDTO.genres = product.getGenreProductList().stream()
                .map(genreProduct -> {
                    Genre genre = genreProduct.getGenre();
                    return genre.getName();})
                .toList();
        productDetailDTO.content = product.getContent();
        productDetailDTO.createAt = product.getCreateAt();
        productDetailDTO.modifyAt = product.getModifyAt();

        return productDetailDTO;
    }
}
