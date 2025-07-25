//package somt.somt.domain.product.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.web.multipart.MultipartFile;
//import somt.somt.common.exception.CustomException;
//import somt.somt.common.exception.ErrorCode;
//import somt.somt.domain.genre.entity.Genre;
//import somt.somt.domain.genre.service.GenreService;
//import somt.somt.domain.genreProduct.entity.GenreProduct;
//import somt.somt.domain.product.dto.reponse.ProductDTO;
//import somt.somt.domain.product.dto.reponse.ProductDetailDTO;
//import somt.somt.domain.product.dto.request.ProductRequest;
//import somt.somt.domain.product.entity.Product;
//import somt.somt.domain.product.repository.ProductRepository;
//import somt.somt.domain.productThumbnail.service.ProductThumbnailService;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceUnitTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private ProductService productService;
//
//    @Mock
//    ProductThumbnailService productThumbnailService;
//    @Mock
//    GenreService genreService;
//
//    Product testProduct;
//
//
//
//    @Test
//    void getProductTest(){
//        //product mock 생성
//        Product mockProduct = mock(Product.class);
//        when(mockProduct.getId()).thenReturn(42L);
//        when(mockProduct.getProductName()).thenReturn("로스트 아크");
//        when(mockProduct.getPrice()).thenReturn(new BigDecimal(1200));
//
//        //장르 mock 생성
//        Genre mockGenre = mock(Genre.class);
//        when(mockGenre.getName()).thenReturn("mmorpg");
//
//        //장프 프로턱트 mock 생성
//        GenreProduct mockGenreProduct = mock(GenreProduct.class);
//        when(mockGenreProduct.getGenre()).thenReturn(mockGenre);
//        when(mockProduct.getGenreProductList()).thenReturn(Collections.singletonList(mockGenreProduct));
//
//
//        Page<Product> page = new PageImpl<>(
//                Collections.singletonList(mockProduct),
//                PageRequest.of(0,1,Sort.by("createAt").descending()),
//                1
//        );
//        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
//
//        Map<String,Object> result = productService.getProductSearch("",0,1);
//
//        @SuppressWarnings("nuchecked")
//        List<ProductDTO> content = (List<ProductDTO>) result.get("content");
//        assertThat(content).hasSize(1);
//        ProductDTO productDTO = content.get(0);
//        assertThat(productDTO.getProductName()).isEqualTo("로스트 아크");
//        assertThat(productDTO.getPrice()).isEqualByComparingTo("1200");
//
//        assertThat(productDTO.getGenres()).containsExactly("mmorpg");
//        assertThat(result.get("totalPageCount")).isEqualTo(1);
//        assertThat(result.get("currentPage")).isEqualTo(0);
//    }
//
//    @Test
//    void getProductDetails(){
//        Product mockProduct = mock(Product.class);
//        when(mockProduct.getId()).thenReturn(42L);
//        when(mockProduct.getProductName()).thenReturn("로스트아크");
//        when(mockProduct.getPrice()).thenReturn(new BigDecimal(1200));
//        Genre mockGenre = mock(Genre.class);
//        when(mockGenre.getName()).thenReturn("mmorpg");
//
//        //장프 프로턱트 mock 생성
//        GenreProduct mockGenreProduct = mock(GenreProduct.class);
//        when(mockGenreProduct.getGenre()).thenReturn(mockGenre);
//        when(mockProduct.getGenreProductList()).thenReturn(Collections.singletonList(mockGenreProduct));
//
//
//        when(productRepository.findById(42L)).thenReturn(Optional.of(mockProduct));
//
//        ProductDetailDTO content = productService.getProductDetails(42L);
//
//        assertThat(content.getProductName()).isEqualTo("로스트아크");
//        assertThat(content.getPrice()).isEqualTo("1200");
//
//
//
//    }
//
//
//    @Test
//    void getProductDetails_NotFound(){
//        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(()->productService.getProductDetails(99L))
//                .isInstanceOf(CustomException.class)
//                .extracting("errorCode")
//                .isEqualTo(ErrorCode.NOT_FOUND_PRODUCT);
//
//
//    }
//
//    @Test
//    void create(){
//
//        //given
//        List<MultipartFile> imageFiles = Collections.emptyList();
//        List<String> genres = Collections.emptyList();
//        ProductRequest productRequest = new ProductRequest("아크",new BigDecimal(2000),10,"잼남",List.of("RPG"));
//        when(productRepository.existsByProductName(anyString())).thenReturn(false);
//
//        Product saved = Product.create(productRequest);
//
//        /**
//         * ReflectionTestUtils
//         * Spring Test 라이프러리에서 제공하는 유틸 클래스
//         * 테스트 코드에서 private, protected 필드에 값을 주입할때 사용
//         */
//        ReflectionTestUtils.setField(saved,"id",42L);
//        ReflectionTestUtils.setField(saved,"createAt", LocalDateTime.now());
//        ReflectionTestUtils.setField(saved,"modifyAt",LocalDateTime.now());
//        when(productRepository.save(any())).thenReturn(saved);
//
//
//        Genre genre = new Genre(1L,"RPG");
//        when(genreService.getGere("RPG")).thenReturn(genre);
//
//
//
//       //when
//        ProductDetailDTO testDto = productService.create(productRequest,imageFiles);
//
//
//        //then
//        assertThat(testDto.getPrice()).isEqualTo("2000");
//        assertThat(testDto.getProductName()).isEqualTo("아크");
//        assertThat(testDto.getGenres().get(0)).isEqualTo("RPG");
//
//    }
//
//    @Test
//    void create_existsByProductName(){
//        //given
//        ProductRequest testProductRequest = new ProductRequest(
//                "아크",
//                new BigDecimal("2000"),
//                10,
//                "잼남",
//                Collections.emptyList()
//        );
//
//        when(productRepository.existsByProductName(anyString())).thenReturn(true);
//
//        //when
//        CustomException ex = assertThrows(
//                CustomException.class,
//                ()->productService.create(testProductRequest,Collections.emptyList())
//        );
//
//        //then
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_PRODUCTNAME);
//    }
//
//
//
//    @Test
//    void modify(){
//        //given
//        ProductRequest mockProductRequest =
//                new ProductRequest(
//                        "아크",
//                        new BigDecimal("2000"),
//                        10,
//                        "잼남",
//                        List.of("RPG"));
//        List<MultipartFile> mockImages = Collections.emptyList();
//
//        Product mockProduct =
//                new Product(
//                        1L,
//                        "아크2",
//                        new BigDecimal("2002"),
//                        12,
//                        "잼없음"
//                );
//
//        when(productRepository.findById(any())).thenReturn(Optional.of(mockProduct));
//
//        Genre mockGenre = new Genre(1L,"RPG");
//
//        when(genreService.getGere("RPG")).thenReturn(mockGenre);
//        /*
//
//         */
//        doNothing().when(productThumbnailService).modifyImageFile(anyList(),any(Product.class));
//
//        //when
//        Long id = productService.modify(mockProductRequest,1L,mockImages);
//
//        //then
//
//        assertThat(id).isEqualTo(1L);
//        assertThat(mockProduct.getGenreProductList().size()).isEqualTo(1);
//        assertThat(mockProduct.getProductName()).isEqualTo("아크");
//
//        verify(productRepository).findById(1L);
//        verify(genreService).getGere("RPG");
//
//    }
//
//
//
//}
