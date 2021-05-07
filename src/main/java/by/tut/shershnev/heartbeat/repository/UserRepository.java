package by.tut.shershnev.heartbeat.repository;

import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUsername(String username);

}
