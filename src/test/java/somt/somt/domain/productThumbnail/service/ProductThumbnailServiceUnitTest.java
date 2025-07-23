package somt.somt.domain.productThumbnail.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.productThumbnail.repository.ProductThumbnailRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductThumbnailServiceUnitTest {

    @Mock
    private ProductThumbnailRepository productThumbnailRepository;

    @InjectMocks
    private ProductThumbnailService productThumbnailService;

    @Test
    void uploadImageFile() throws IOException {
        // given
        Product product = new Product(1L, "Test Product", new BigDecimal("10.00"), 100, "Test Description");
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> imageFiles = Collections.singletonList(imageFile);

        // when
        productThumbnailService.uploadImageFile(imageFiles, product);

        // then
        verify(productThumbnailRepository).save(any());
    }

    @Test
    void modifyImageFile() throws IOException {
        // given
        Product product = new Product(1L, "Test Product", new BigDecimal("10.00"), 100, "Test Description");
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> imageFiles = Collections.singletonList(imageFile);

        // when
        productThumbnailService.modifyImageFile(imageFiles, product);

        // then
        verify(productThumbnailRepository).save(any());
    }
}
