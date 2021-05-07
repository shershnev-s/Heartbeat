package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.service.HeartBeatBotService;
import by.tut.shershnev.heartbeat.service.HeartBeatService;

import by.tut.shershnev.heartbeat.service.HeartBeatStarterService;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static by.tut.shershnev.heartbeat.service.constant.ServiceConstant.*;

@Service
public class HeartBeatServiceImpl implements HeartBeatService {


    private static final Logger logger = LogManager.getLogger();
    private static final List<String> IP_ADDRESSES_FOR_DELETE = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, String> STATUSES = Collections.synchronizedMap(new HashMap<>());
    private final HeartBeatBotService heartBeatBotService;

    public HeartBeatServiceImpl(HeartBeatBotService heartBeatBotService) {
        this.heartBeatBotService = heartBeatBotService;
    }

    @Override
    public void startIPAttainabilityCheckingThread(String addedIpAddress) {
        Runnable pingTask = () -> {
            STATUSES.put(addedIpAddress, " collecting data");
            List<Boolean> isReachablePool = new ArrayList<>();
            int collectingDataCounter = 0;
            while (!IP_ADDRESSES_FOR_DELETE.contains(addedIpAddress)) {
                collectingDataCounter++;
                try {
                    boolean reachable = InetAddress.getByName(addedIpAddress).isReachable(REACHABILITY_CONDITION);
                    isReachablePool.add(reachable);
                    collectingDataCounter = checkHostAttainability(collectingDataCounter, isReachablePool, addedIpAddress);
                    if (reachable) {
                        STATUSES.put(addedIpAddress, " is online ");
                        logger.info(addedIpAddress + " is online");
                    } else {
                        STATUSES.put(addedIpAddress, " is offline ");
                        logger.info(addedIpAddress + " is offline");
                    }
                    Thread.sleep(2000);
                } catch (IOException | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            IP_ADDRESSES_FOR_DELETE.remove(addedIpAddress);
        };
        new Thread(pingTask).start();
    }

    @Override
    public void removeIPAddress(String ipAddress) {
        IP_ADDRESSES_FOR_DELETE.add(ipAddress);
        logger.info(ipAddress + " was removed from pool");
    }

    @Override
    public String getCurrentStatuses() {
        String allStatuses = "";
        allStatuses = removeUnnecessarySymbolsAndSetTextToMessage(STATUSES);
        return allStatuses;
    }

    @Override
    public String getStatusForIPAddress(String ipAddress) {
        String result = STATUSES.get(ipAddress);
        return result;
    }

    private String removeUnnecessarySymbolsAndSetTextToMessage(Map<String, String> statuses) {
        String resultWithoutBrackets = statuses.toString();
        String result = "";
        resultWithoutBrackets = resultWithoutBrackets.replaceAll("[{}]", "");
        if (!resultWithoutBrackets.isEmpty()) {
            result = resultWithoutBrackets.replaceAll("=", " ");
        }
        return result;
    }

    private int checkHostAttainability(int collectingDataCounter, List<Boolean> isReachablePool, String ipAddress) throws InterruptedException, IOException {
        if (collectingDataCounter >= COLLECTING_DATA_COUNTER_LIMIT) {
            for (int i = 0; i < isReachablePool.size(); i++) {
                if (!isReachablePool.contains(true)) {
                    heartBeatBotService.sendMessageToBot(ipAddress, "IS+UNREACHABLE!");
                    STATUSES.put(ipAddress, " IS UNREACHABLE! ");
                    logger.info(ipAddress + " IS UNREACHABLE!");
                    while (!IP_ADDRESSES_FOR_DELETE.contains(ipAddress)) {
                        Thread.sleep(2000);
                        boolean reachable = InetAddress.getByName(ipAddress).isReachable(REACHABILITY_CONDITION);
                        if (reachable) {
                            heartBeatBotService.sendMessageToBot(ipAddress, "AGAIN+REACHABLE!");
                            STATUSES.put(ipAddress, " IS AGAIN REACHABLE! ");
                            logger.info(ipAddress + " IS AGAIN REACHABLE!");
                            break;
                        }
                    }
                }
                isReachablePool.clear();
            }
            return 0;
        }
        return collectingDataCounter;
    }
}
