package by.tut.shershnev.heartbeat.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"by.tut.shershnev.heartbeat.repository",
		"by.tut.shershnev.heartbeat.service",
		"by.tut.shershnev.heartbeat.controller"})
@EnableJpaRepositories("by.tut.shershnev.heartbeat.repository")
@EntityScan("by.tut.shershnev.heartbeat.repository.model")
public class ServerHeartbeatApplication {

public static void main(String[] args) {
	SpringApplication.run(ServerHeartbeatApplication.class, args);
	}

}
