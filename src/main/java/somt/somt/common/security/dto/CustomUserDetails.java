package somt.somt.common.security.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    final private CustomUserData customUserData;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new SimpleGrantedAuthority( customUserData.getRole()));
        return collection;
    }

    public Long getMemberId() {
        return customUserData.getMemberId();
    }

    public String getRole() {
        return customUserData.getRole();
    }


    @Override
    public String getPassword() {
        return customUserData.getPassword();
    }

    @Override
    public String getUsername() {
        return customUserData.getMemberName();
    }

    public String getNickname() {
        return customUserData.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}