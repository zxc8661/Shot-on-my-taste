package somt.somt.domain.genreProduct.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.genreProduct.repository.GenreProductRepository;
import somt.somt.domain.product.entity.Product;

@Service
@RequiredArgsConstructor
public class GenreProductService {
    private final GenreProductRepository genreProductRepository;

    public GenreProduct create(Genre genre, Product product){
        GenreProduct genreProduct = new GenreProduct(genre,product);
        genreProductRepository.save(genreProduct);

        return genreProduct;
    }


    public Genre getGere(Product product){
        return genreProductRepository.findByProduct(product);
    }

    public Product getProduct(Genre genre){
        return genreProductRepository.findByGenre(genre);
    }



}
