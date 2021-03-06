package by.tut.shershnev.server.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ServerHeartbeatApplication {

/*	public static void main(String[] args) throws IOException {
		SpringApplication.run(ServerHeartbeatApplication.class, args);
		//PingServiceImpl serverService = new PingServiceImpl();
		InetAddress inetAddress1 = InetAddress.getByName("10.10.20.210");
		InetAddress inetAddress2 = InetAddress.getByName("10.10.10.5");
			new PingServiceImpl(inetAddress1).start();
			new PingServiceImpl(inetAddress2).start();
	}*/
public static void main(String[] args) {
	SpringApplication.run(ServerHeartbeatApplication.class, args);
	}

}
