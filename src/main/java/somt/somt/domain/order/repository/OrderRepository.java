package somt.somt.domain.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
