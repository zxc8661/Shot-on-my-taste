package somt.somt.domain.product.service;

import jakarta.transaction.Transactional;
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
public class ProductRepositorySearchByKeywordTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    void test(){

        Long startTime = System.nanoTime();

        for(int i=0;i<100;i++){

            productService.getProductSearch("",0,10);


        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double averageDurationMillis = TimeUnit.NANOSECONDS.toMillis(duration) / (double) 100;

        System.out.println("수정 전 searchByKeyword 평균 실행 시간: " + averageDurationMillis + " ms");
        assertThat(averageDurationMillis).isPositive();
    }

}
