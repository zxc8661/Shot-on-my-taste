package somt.somt.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.product.entity.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "order_detail")
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private Integer amount;

    public OrderDetail(Product product,Order order,Integer amount){
        this.product = product;
        this.order = order;
        this.createAt  = LocalDateTime.now();
        this.amount = amount;
    }

}
