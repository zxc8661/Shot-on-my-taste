package somt.somt.domain.product.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.product.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    private String productName;

    private BigDecimal price;

    private String img;

    private List<String> genres = new ArrayList<>();

    private Double grade ;


    public ProductDTO(Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.img = product.getProductThumbnails().stream().toList().get(0).getImagePath();
        this.genres = product.getGenreProductList().stream()
                .map(genreProduct->{
                    return genreProduct.getGenre().getName();
                }).toList();
        this.grade = product.getCommentList().stream().mapToInt(Comment::getGrade).average().orElse(0.0);
    }
}
