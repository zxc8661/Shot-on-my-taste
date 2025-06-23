package somt.somt.domain.address.entity;

import jakarta.persistence.*;
import lombok.Getter;
import somt.somt.domain.member.entity.Member;

@Entity
@Getter
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String address;
}
