package somt.somt.domain.product.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import somt.somt.domain.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByProductName(String productName);


    boolean existsByProductName(String productName);

    Page<Product> findByProductNameContainingIgnoreCaseOrContentContainingIgnoreCase(String keyword1,String keyword2,Pageable pageable );

    @Query("""
            select p
            from Product p
            where lower(p.productName) like lower(concat('%', :kw, '%'))
            or  lower(p.content) like lower(concat('%', :kw,'%'))
            """)
    Page<Product> searchByKeyword(@Param("kw") String keyword, Pageable pageable);
}
