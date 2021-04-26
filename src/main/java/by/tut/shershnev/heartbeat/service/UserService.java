package by.tut.shershnev.heartbeat.service;

import by.tut.shershnev.heartbeat.repository.model.UserAccount;

public interface UserService {

    UserAccount loadUserByUsername(String username);

}
