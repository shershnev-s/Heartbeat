package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.repository.UserRepository;
import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import by.tut.shershnev.heartbeat.service.UserService;
import by.tut.shershnev.heartbeat.service.model.HBUser;
import by.tut.shershnev.heartbeat.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger();
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserAccount loadUserByUsername(String username) {
        UserAccount user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public String changePassword(UserDTO userDTO) {
        boolean isExists = checkOldPassword(userDTO);
        boolean isEquals;
        String username;
        String password = userDTO.getConfirmingPassword();
        if (isExists) {
            isEquals = checkNewPassword(userDTO);
            if (isEquals) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                username = ((HBUser) principal).getUsername();
                UserAccount userAccount = userRepository.findByUsername(username);
                userAccount.setPassword(passwordEncoder.encode(password));
                userRepository.save(userAccount);
                return "Password was changed";
            } else {
                return "New password do not match with confirming one";
            }
        } else {
            logger.warn("Wrong password was entered");
            return "Wrong password";
        }
    }

    private boolean checkOldPassword(UserDTO userDTO) {
        String oldPassword = userDTO.getOldPassword();
        String currentPassword;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentPassword = ((HBUser) principal).getPassword();
        boolean matches = passwordEncoder.matches(oldPassword,currentPassword);
        if (matches)
        return true;
        else {
            return false;
        }
    }

    private boolean checkNewPassword(UserDTO userDTO) {
        String newPassword = userDTO.getNewPassword();
        String confirmingPassword = userDTO.getConfirmingPassword();
        return newPassword.equals(confirmingPassword);
    }


}
