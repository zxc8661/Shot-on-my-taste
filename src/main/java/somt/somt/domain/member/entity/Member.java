package somt.somt.domain.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.address.entity.Address;
import somt.somt.domain.cart.entity.Cart;
import somt.somt.domain.comment.entity.Comment;
import somt.somt.domain.notification.entity.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name= "member")
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "user_name", nullable = false, length = 50,unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, length = 50,unique = true)
    private String nickname;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;


    @Column(name = "is_active",nullable = false)
    private Boolean isActive;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "modify_at",nullable = false)
    private LocalDateTime modifyAt;


    /**
     * mappedBy = "member" -> 연관관계의 주인이 cart이므로 필요
     * cascade = CascadeType.ALL -> Member 삭제시 카트도 같이 삭제 가능
     * orphanRemoval = true 부모엔티티가 삭데
     */
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true )
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Address> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Notification> notificationList = new ArrayList<>();

    static public Member create (String userName, String password,String nickName, String email, String role){
      Member member = new Member();
      member.userName = userName;
      member.password = password;
      member.nickname = nickName;
      member.email = email;
      member.role = role;
      member.isActive=true;
      member.createAt = LocalDateTime.now();
      member.modifyAt = LocalDateTime.now();
      return member;
    }



    public void setModifyAt(){
        this.modifyAt = LocalDateTime.now();
    }

    public void setIsActive(){
        this.isActive = false;
    }

    public void modifyEmail(String email) {
        this.email = email;
        this.modifyAt = LocalDateTime.now();
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
        this.modifyAt = LocalDateTime.now();
    }

    public void modifyPassword(String encode) {
        this.password = encode;
        this.modifyAt = LocalDateTime.now();
    }

    // 단위 테스트용 생성자
    public Member(CustomUserDetails customUserDetails){
        this.id = customUserDetails.getMemberId();
        this.userName = customUserDetails.getUsername();
        this.password = customUserDetails.getPassword();
        this.nickname = customUserDetails.getNickname();
        this.email = " ";
        this.role = customUserDetails.getRole();
        this.isActive=true;
        this.createAt = LocalDateTime.now();
        this.modifyAt = LocalDateTime.now();
    }
}
