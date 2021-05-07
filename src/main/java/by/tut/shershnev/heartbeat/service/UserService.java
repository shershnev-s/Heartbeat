package by.tut.shershnev.heartbeat.service;

import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import by.tut.shershnev.heartbeat.service.model.UserDTO;

public interface UserService {

    UserAccount loadUserByUsername(String username);

    String changePassword(UserDTO userDTO);
}
