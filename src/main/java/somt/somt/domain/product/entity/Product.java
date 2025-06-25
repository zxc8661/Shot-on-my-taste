package somt.somt.domain.product.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.product.dto.request.ProductRequest;

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


    @Column(name = "product_name",nullable = false)
    private String productName;

    @Column(name = "price" ,nullable = false)
    private BigDecimal price;

    @Column(name = "stock" ,nullable = false)
    private Integer stock;


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


    public static Product create(ProductRequest productCreate){
        Product product = new Product();
        product.productName= productCreate.getProductName();
        product.content = productCreate.getContent();
        product.stock = productCreate.getStock();
        product.price = productCreate.getPrice();
        product.createAt = LocalDateTime.now();
        product.modifyAt=LocalDateTime.now();
        return product;

    }

    public void updateImages(List<String> filePaths) {
        if (filePaths.size() > 0) this.img1 = filePaths.get(0);
        if (filePaths.size() > 1) this.img2 = filePaths.get(1);
        if (filePaths.size() > 2) this.img3 = filePaths.get(2);
        if (filePaths.size() > 3) this.img4 = filePaths.get(3);
        if (filePaths.size() > 4) this.img5 = filePaths.get(4);
    }

    public void modifyBasicInfo(String productName, String content,BigDecimal price, Integer stock) {

        this.productName =productName;
        this.content=content;
        this.price =price;
        this.stock =stock;
        this.modifyAt = LocalDateTime.now();
    }
}
