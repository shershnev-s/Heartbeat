package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.repository.UserAccountRepository;
import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import by.tut.shershnev.heartbeat.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userRepository;

    public UserServiceImpl(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserAccount loadUserByUsername(String username) {
        UserAccount user = userRepository.findByUsername(username);
        return userRepository.findByUsername(username);
    }



}
