package somt.somt.domain.cart.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.product.entity.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer amount;


    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;


    public Cart(Member member,Product product, Integer amount){
        this.member = member;
        this.product = product;
        this.amount = amount;
        this.createAt = LocalDateTime.now();
    }
}
