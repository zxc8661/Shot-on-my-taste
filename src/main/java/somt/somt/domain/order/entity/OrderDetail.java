package somt.somt.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import somt.somt.domain.product.entity.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

}
