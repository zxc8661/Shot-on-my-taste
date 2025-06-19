package somt.somt.common.security.dto;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    final private CustomUserData customUserData;

    public CustomUserDetails(
            CustomUserData customUserData) {
        this.customUserData = customUserData;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return customUserData.getRole();
            }
        });
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