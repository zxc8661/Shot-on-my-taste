package somt.somt.domain.comment.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.member.entity.Member;
import somt.somt.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Column(nullable = false)
    private String content;

    private Integer grade;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToMany
    private Set<Member> likedMembers = new HashSet<>();


    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name="modify_at" ,nullable = false)
    private LocalDateTime modifyAt;
}
