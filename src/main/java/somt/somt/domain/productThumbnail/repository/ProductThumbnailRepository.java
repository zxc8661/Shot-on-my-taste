package somt.somt.domain.productThumbnail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.productThumbnail.entity.ProductThumbnail;

@Repository
public interface ProductThumbnailRepository extends JpaRepository<ProductThumbnail,Long> {
}
