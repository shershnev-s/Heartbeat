package by.tut.shershnev.heartbeat.service;

import java.io.IOException;
import java.util.Map;

public interface HeartBeatBotService {

    void setIPStatus(String ipAddress, String status);

    void removeIPStatus(String ipAddress);

    void sendMessageToBot(String ipAddress, String message) throws IOException;

    String getIPStatus(String ipAddress);

}
