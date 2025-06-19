package somt.somt.member.entity;


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

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nick_name", nullable = false, length = 50)
    private String nickName;

    @Column(name = "role", nullable = false)
    private String role;

    private String email;

    private

    @Column(name = "is_active",nullable = false)
    private int isActive;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    static public Member create (String nickName,
                                 String userName,
                                 String password){
        Member user = new Member();
        user.nickName = nickName;
        user.userName = userName;
        user.password = password;
        user.role = "CUSTOMER";
        user.isActive =1;
        user.createAt= LocalDateTime.now();
        return user;
    }

}
