package somt.somt.domain.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name= "users")
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long addressId;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nick_name", nullable = false, length = 50)
    private String nickName;

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

    static public Member create (String userName, String password,String nickName, String email, String role){
      Member member = new Member();
      member.userName = userName;
      member.password = password;
      member.nickName = nickName;
      member.email = email;
      member.role = role;
      member.isActive=true;
      member.createAt = LocalDateTime.now();
      member.modifyAt = LocalDateTime.now();
      return member;
    }

}
