package somt.somt.domain.address.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.address.dto.AddressRequest;
import somt.somt.domain.member.entity.Member;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String address;

    public Address(Member member,String address){
        this.member = member;
        this.address = address;
    }

    public void modify(String address) {
        this.address = address;
    }
}
