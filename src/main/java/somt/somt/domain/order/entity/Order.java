package somt.somt.domain.order.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.member.entity.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name="create_at")
    private LocalDateTime createAt;

    @Column(name="modify_at")
    private LocalDateTime modifyAt;

    public Order(Member member){
        this.member = member;
        this.totalPrice =new BigDecimal(0);
        this.createAt = LocalDateTime.now();
        this.modifyAt = LocalDateTime.now();

    }

    public void modifyTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }

    public void addOrderDetail(OrderDetail orderDetail){
        this.orderDetailList.add(orderDetail);
    }


}
