package by.tut.shershnev.heartbeat.service;

import by.tut.shershnev.heartbeat.service.model.InetAddressDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

public interface HeartBeatStarterService {

    void startIPAttainabilityChecking(InetAddressDTO inetAddressDTO) throws IOException, InterruptedException, ExecutionException;

    CopyOnWriteArrayList<InetAddressDTO> getAllAddresses() throws InterruptedException;

    void removeIP(InetAddressDTO inetAddressDTO);

    List<String> getCurrentCheckedIPAddresses();
}
