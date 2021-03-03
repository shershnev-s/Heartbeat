package by.tut.shershnev.server.service;

import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.quartz.JobExecutionContext;

import java.io.IOException;

public interface PingStarterService {

    void startPing(InetAddressDTO inetAddressDTO) throws IOException, InterruptedException;


}
