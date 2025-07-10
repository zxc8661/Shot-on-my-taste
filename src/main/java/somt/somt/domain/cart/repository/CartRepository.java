package somt.somt.domain.cart.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.cart.entity.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    Cart findByMemberIdAndProductId(Long memberId,Long productId);

    void deleteAllByMemberId(Long memberId);
}
