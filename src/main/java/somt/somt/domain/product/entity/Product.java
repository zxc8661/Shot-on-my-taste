package somt.somt.domain.product.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.genre.entity.GenreProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "price" ,nullable = false)
    private BigDecimal price;

    @Column(name = "number" ,nullable = false)
    private Integer number;


    private String img1;

    private String img2;

    private String img3;
    private String img4;

    private String img5;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product",orphanRemoval = true)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<GenreProduct> genreProductList;

    @Column(name = "content" ,nullable = false)
    private String content;

    @Column(name = "create_at" ,nullable = false)
    private LocalDateTime createAt;

    @Column(name = "modify_at" ,nullable = false)
    private LocalDateTime modifyAt;
}
