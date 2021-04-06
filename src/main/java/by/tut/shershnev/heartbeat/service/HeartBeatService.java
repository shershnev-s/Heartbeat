package by.tut.shershnev.heartbeat.service;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public interface HeartBeatService {

    void startIPAttainabilityCheckingThread(String ipAddress) throws InterruptedException, ExecutionException, UnknownHostException;

    String doSingleIPCheck(String ipAddress);

    void removeIpAddress(String ipAddress);

}
