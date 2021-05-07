package by.tut.shershnev.heartbeat.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public interface HeartBeatService {

    void startIPAttainabilityCheckingThread(String ipAddress) throws InterruptedException, ExecutionException, UnknownHostException;

    String getStatusForIPAddress(String ipAddress);

    void removeIPAddress(String ipAddress);

    String getCurrentStatuses() throws InterruptedException, IOException;

}
