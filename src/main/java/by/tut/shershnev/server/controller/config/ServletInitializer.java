package by.tut.shershnev.server.controller.config;

import by.tut.shershnev.server.controller.ServerHeartbeatApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServerHeartbeatApplication.class);

	}

}
