package somt.somt.domain.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.image.ImageHandler;
import somt.somt.domain.genre.entity.Genre;
import somt.somt.domain.genre.service.GenreService;
import somt.somt.domain.genreProduct.entity.GenreProduct;
import somt.somt.domain.genreProduct.service.GenreProductService;
import somt.somt.domain.product.dto.request.ProductRequest;
import somt.somt.domain.product.dto.reponse.ProductDetailDTO;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.repository.ProductRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ImageHandler imageHandler;
    @Mock
    private GenreService genreService;
    @Mock
    private GenreProductService genreProductService;

    private Product sampleProduct;
    private Genre sampleGenre;
    private GenreProduct sampleGenreProduct;

    @BeforeEach
    void setUp() throws Exception {
        sampleProduct = new Product(
                1L,
                "테스트 상품",
                new BigDecimal("1000"),
                5,
                null, null, null, null, null,
                Collections.emptyList(),
                new ArrayList<>(),
                "내용",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sampleGenre = new Genre(
                1L,
                "장르1",
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        sampleGenreProduct = new GenreProduct(
                1L,
                sampleGenre,
                sampleProduct
        );
    }

    @Test
    void getProducts() {
        // given
        Page<Product> page = new PageImpl<>(List.of(sampleProduct), PageRequest.of(0, 1), 1);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        // when
        Map<String, Object> result = productService.getProducts(0, 1);

        // then
        assertTrue(result.containsKey("content"));
        var content = (List<?>) result.get("content");
        assertEquals(1, content.size());
        assertEquals(1, result.get("totalPageCount"));
        assertEquals(1L, result.get("totalElementCount"));
        assertEquals(0, result.get("currentPage"));
        verify(productRepository).findAll(PageRequest.of(0, 1));
    }

    @Test
    void getProductDetails_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductDetailDTO dto = productService.getProductDetails(1L);

        assertEquals(sampleProduct.getId(), dto.getId());
        assertEquals(sampleProduct.getProductName(), dto.getProductName());
        assertEquals(sampleProduct.getContent(), dto.getContent());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductDetails_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(
                CustomException.class,
                () -> productService.getProductDetails(2L)
        );
        assertEquals(ErrorCode.NOT_FOUND_PRODUCT, ex.getErrorCode());
    }

    @Test
    void create_Success() throws IOException, NoSuchFieldException, IllegalAccessException {
        // given
        ProductRequest req = new ProductRequest(
                "새상품",
                new BigDecimal("2000"),
                10,
                "내용",
                List.of("장르1")
        );
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(imageHandler.saveImage(mockFile)).thenReturn("path1.jpg");
        when(genreService.getGere("장르1")).thenReturn(sampleGenre);
        when(genreProductService.create(eq(sampleGenre), any(Product.class)))
                .thenReturn(sampleGenreProduct);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            var idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(p, 100L);
            return p;
        });

        // when
        productService.create(req, List.of(mockFile));

        // then
        verify(productRepository, times(1)).save(any(Product.class));
        verify(imageHandler).saveImage(mockFile);
        verify(genreService).getGere("장르1");
        verify(genreProductService).create(eq(sampleGenre), any(Product.class));
    }

    @Test
    void modify_Success() throws IOException {
        ProductRequest req = new ProductRequest(
                "수정상품",
                new BigDecimal("3000"),
                15,
                "수정내용",
                List.of("장르2")
        );
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(imageHandler).deleteImage(anyList());
        when(imageHandler.saveImage(mockFile)).thenReturn("newpath.jpg");
        when(genreService.getGere("장르2")).thenReturn(sampleGenre);
        when(genreProductService.create(eq(sampleGenre), eq(sampleProduct)))
                .thenReturn(sampleGenreProduct);

        // when
        productService.modify(req, 1L, List.of(mockFile));

        // then
        assertEquals("수정상품", sampleProduct.getProductName());
        assertEquals("수정내용", sampleProduct.getContent());
        assertEquals(new BigDecimal("3000"), sampleProduct.getPrice());
        assertEquals(15, sampleProduct.getStock());
        verify(imageHandler).deleteImage(anyList());
        verify(imageHandler).saveImage(mockFile);
        verify(genreService).getGere("장르2");
    }

    @Test
    void delete_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        productService.delete(1L);

        verify(productRepository).delete(sampleProduct);
    }

    @Test
    void delete_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(
                CustomException.class,
                () -> productService.delete(2L)
        );
        assertEquals(ErrorCode.NOT_FOUND_PRODUCT, ex.getErrorCode());
    }
}
