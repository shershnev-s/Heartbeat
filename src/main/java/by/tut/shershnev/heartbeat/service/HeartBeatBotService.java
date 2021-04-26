package by.tut.shershnev.heartbeat.service;

import java.io.IOException;
import java.util.Map;

public interface HeartBeatBotService {

    void sendMessageToBot(String ipAddress, String message) throws IOException;

    String getIPStatus();

}
