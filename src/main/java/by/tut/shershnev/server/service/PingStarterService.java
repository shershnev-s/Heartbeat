package by.tut.shershnev.server.service;

import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface PingStarterService {

    void ping(InetAddressDTO inetAddress) throws SchedulerException, IOException;
}
