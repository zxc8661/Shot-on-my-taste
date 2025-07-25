package somt.somt.domain.product.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.productThumbnail.entity.ProductThumbnail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "product_name",nullable = false,unique = true)
    private String productName;

    @Column(name = "price" ,nullable = false)
    private BigDecimal price;

    @Column(name = "stock" ,nullable = false)
    private Integer stock;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product",orphanRemoval = true)
    private Set<ProductThumbnail> productThumbnails = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product",orphanRemoval = true)
    private Set<Comment> commentList =new HashSet<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<GenreProduct> genreProductList= new HashSet<>();

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



    public void modifyBasicInfo(String productName, String content,BigDecimal price, Integer stock) {

        this.productName =productName;
        this.content=content;
        this.price =price;
        this.stock =stock;
        this.modifyAt = LocalDateTime.now();
    }

    public void addGenreProduct(Genre genre){
        GenreProduct genreProduct = new GenreProduct(genre,this);
        this.genreProductList.add(genreProduct);
    }

    public Product(Long id,String productName,BigDecimal price,int stock,String content){
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.content = content;
        this.stock = stock;
        this.createAt = LocalDateTime.now();
        this.modifyAt= LocalDateTime.now();
    }
}
