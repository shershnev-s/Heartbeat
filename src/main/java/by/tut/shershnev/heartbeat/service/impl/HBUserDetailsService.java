package by.tut.shershnev.heartbeat.service.impl;

/*
import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import by.tut.shershnev.heartbeat.service.UserService;
import by.tut.shershnev.heartbeat.service.model.HBUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HBUserDetailsService implements UserDetailsService{

    private final UserService userService;

    public HBUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userService.loadUserByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new HBUser(userAccount);
    }
}
*/
