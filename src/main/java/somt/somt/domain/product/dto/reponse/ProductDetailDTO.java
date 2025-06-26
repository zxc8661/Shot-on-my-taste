package somt.somt.domain.product.dto.reponse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.product.entity.Product;

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


    public static ProductDetailDTO exchange(Product product)
    {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.productName = product.getProductName();
        productDetailDTO.stock = product.getStock();

        productDetailDTO.id=product.getId();
        productDetailDTO.genres= product.getGenreProductList().stream()
                .map(gp-> gp.getGenre().getName())
                .toList();

        productDetailDTO.images= new ArrayList<>();
        productDetailDTO.images.add(product.getImg1());
        productDetailDTO.images.add(product.getImg2());
        productDetailDTO.images.add(product.getImg4());
        productDetailDTO.images.add(product.getImg3());
        productDetailDTO.images.add(product.getImg5());



        productDetailDTO.content = product.getContent();

        productDetailDTO.createAt= product.getCreateAt();
        productDetailDTO.modifyAt=product.getModifyAt();

        return productDetailDTO;
    }
}
