package somt.somt.common.security.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import somt.somt.common.security.dto.CustomUserDetails;


public class AuthorityCheck {

    public static  boolean isAdmin(CustomUserDetails user){
        return user != null && "ADMIN".equals(user.getRole());
    }
}
