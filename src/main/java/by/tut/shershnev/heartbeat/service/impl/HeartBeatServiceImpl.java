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
    private final HeartBeatStarterService heartBeatStarterService;

    public HeartBeatServiceImpl(HeartBeatBotService heartBeatBotService, HeartBeatStarterService heartBeatStarterService) {
        this.heartBeatBotService = heartBeatBotService;
        this.heartBeatStarterService = heartBeatStarterService;
    }

    @Override
    public void startIPAttainabilityCheckingThread(String addedIpAddress) {
        Runnable pingTask = () -> {
            List<Boolean> isReachablePool = new ArrayList<>();
            int collectingDataCounter = 0;
            while (!IP_ADDRESSES_FOR_DELETE.contains(addedIpAddress)) {
                System.out.println(Thread.currentThread().getName() + " " + IP_ADDRESSES_FOR_DELETE);
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
    public String doSingleIPCheck(String ipAddress) {
        return heartBeatBotService.getIPStatus(ipAddress);
    }

    @Override
    public void removeIpAddress(String ipAddress) {
        IP_ADDRESSES_FOR_DELETE.add(ipAddress);
        logger.info(ipAddress + " was removed from pool");
    }

    @Override
    public String getCurrentStatuses() {
        String allStatuses = "";
        allStatuses = removeUnnecessarySymbolsAndSetTextToMessage(STATUSES);
        return allStatuses;
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
                    //heartBeatBotService.setIPStatus(ipAddress, "IS UNREACHABLE!!");
                    STATUSES.put(ipAddress, " IS UNREACHABLE! ");
                    logger.info(ipAddress + " IS UNREACHABLE!");
                    while (!IP_ADDRESSES_FOR_DELETE.contains(ipAddress)) {
                        Thread.sleep(2000);
                        boolean reachable = InetAddress.getByName(ipAddress).isReachable(REACHABILITY_CONDITION);
                        if (reachable) {
                            heartBeatBotService.sendMessageToBot(ipAddress, "AGAIN+REACHABLE!");
                            //heartBeatBotService.setIPStatus(ipAddress, " IS AGAIN REACHABLE!");
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
