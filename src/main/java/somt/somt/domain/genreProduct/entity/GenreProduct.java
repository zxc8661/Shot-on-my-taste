package somt.somt.domain.genreProduct.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.product.entity.Product;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
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


    public GenreProduct(Genre genre, Product product) {
        this.genre  =genre;
        this.product = product;
    }
}
