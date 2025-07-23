package somt.somt.domain.genreProduct.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.genreProduct.repository.GenreProductRepository;
import somt.somt.domain.product.entity.Product;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreProductServiceUnitTest {

    @Mock
    private GenreProductRepository genreProductRepository;

    @InjectMocks
    private GenreProductService genreProductService;

    @Test
    void create() {
        // given
        Genre genre = new Genre(1L, "Action");
        Product product = new Product(1L, "Test Product", new BigDecimal("10.00"), 100, "Test Description");
        GenreProduct genreProduct = new GenreProduct(genre, product);

        when(genreProductRepository.save(any(GenreProduct.class))).thenReturn(genreProduct);

        // when
        GenreProduct result = genreProductService.create(genre, product);

        // then
        assertThat(result.getId()).isEqualTo(genreProduct.getId());
        verify(genreProductRepository).save(any(GenreProduct.class));
    }

    @Test
    void getGere() {
        // given
        Product product = new Product(1L, "Test Product", new BigDecimal("10.00"), 100, "Test Description");
        Genre genre = new Genre(1L, "Action");

        when(genreProductRepository.findByProduct(product)).thenReturn(genre);

        // when
        Genre result = genreProductService.getGere(product);

        // then
        assertThat(result).isEqualTo(genre);
        verify(genreProductRepository).findByProduct(product);
    }

    @Test
    void getProduct() {
        // given
        Genre genre = new Genre(1L, "Action");
        Product product = new Product(1L, "Test Product", new BigDecimal("10.00"), 100, "Test Description");

        when(genreProductRepository.findByGenre(genre)).thenReturn(product);

        // when
        Product result = genreProductService.getProduct(genre);

        // then
        assertThat(result).isEqualTo(product);
        verify(genreProductRepository).findByGenre(genre);
    }
}
