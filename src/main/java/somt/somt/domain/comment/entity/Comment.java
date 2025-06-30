package somt.somt.domain.comment.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.comment.dto.CommentRequest;
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

    public Comment(Member member, Product product,String content,Integer grade){
        this.member = member;
        this.product = product;
        this.content = content;
        this.grade = grade;
        this.createAt = LocalDateTime.now();
        this.modifyAt = LocalDateTime.now();
    }

    public Comment(Member member, Product product,String content,Integer grade,Long parentId){
        this.member = member;
        this.product = product;
        this.content = content;
        this.grade = grade;
        this.parentId = parentId;
        this.createAt = LocalDateTime.now();
        this.modifyAt = LocalDateTime.now();
    }

    public void modify(CommentRequest commentRequest) {
        this.content= commentRequest.getContent();
        this.grade = commentRequest.getGrade();
        this.modifyAt  = LocalDateTime.now();
    }
}
