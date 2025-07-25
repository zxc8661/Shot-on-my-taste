package somt.somt.domain.product.service;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.product.repository.ProductRepository;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ProductRepositorysearchTest {

    @Autowired
    ProductRepository productRepository;


    @Test
    void test(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("createAt").descending());

        Long startTime = System.nanoTime();

        for(int i=0;i<100;i++){
            Page<Product> products = productRepository.searchByKeyword("",pageable);

            products.getContent().forEach(product -> {
                                 product.getProductName();
                                 product.getContent();
                                 product.getGenreProductList().size(); // fetch join된 컬렉션 접근
                                 product.getProductThumbnails().size(); // fetch join된 컬렉션 접근
                             });

        }

        long endTime = System.nanoTime();
                 long duration = endTime - startTime;
                 double averageDurationMillis = TimeUnit.NANOSECONDS.toMillis(duration) / (double) 100;

                 System.out.println("수정 후 searchByKeyword 평균 실행 시간: " + averageDurationMillis + " ms");
        assertThat(averageDurationMillis).isPositive();
    }


}
