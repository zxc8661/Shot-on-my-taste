package somt.somt.domain.genreProduct.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.product.entity.Product;

@Repository
public interface GenreProductRepository extends JpaRepository<GenreProduct,Long> {
    Genre findByProduct(Product product);

    Product findByGenre(Genre genre);
}
