package somt.somt.domain.order.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByMemberId(Long memberId, Pageable pageable);
}
