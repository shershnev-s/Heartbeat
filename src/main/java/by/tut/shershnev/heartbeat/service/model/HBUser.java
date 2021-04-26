package by.tut.shershnev.heartbeat.service.model;

/*
import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HBUser implements UserDetails {

    private final UserAccount user;
    private final List<SimpleGrantedAuthority> authorities;

    public HBUser(UserAccount user) {
        this.user = user;
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                ));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
*/
