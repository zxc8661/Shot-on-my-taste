package somt.somt.domain.genre.entity;


import jakarta.persistence.*;
import lombok.Getter;
import somt.somt.domain.product.entity.Product;

import java.util.UUID;

@Entity
@Getter
@Table(name = "genre_product")
public class GenreProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
