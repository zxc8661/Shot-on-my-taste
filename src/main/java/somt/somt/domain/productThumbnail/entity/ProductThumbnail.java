package somt.somt.domain.productThumbnail.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.product.entity.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_thumbnail")
@Builder
public class ProductThumbnail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path",nullable = false)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;


    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;



}
