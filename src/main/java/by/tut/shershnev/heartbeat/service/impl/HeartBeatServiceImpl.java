package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.service.HeartBeatBotService;
import by.tut.shershnev.heartbeat.service.HeartBeatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static by.tut.shershnev.heartbeat.service.constant.ServiceConstant.*;

@Service
public class HeartBeatServiceImpl implements HeartBeatService {


    private static final Logger logger = LogManager.getLogger();
    private static final CopyOnWriteArrayList<String> IP_ADDRESSES_FOR_DELETE = new CopyOnWriteArrayList<>();
    private final HeartBeatBotService heartBeatBotService;

    public HeartBeatServiceImpl(HeartBeatBotService heartBeatBotService) {
        this.heartBeatBotService = heartBeatBotService;
    }

    @Override
    public void startIPAttainabilityCheckingThread(String addedIpAddress) {
        Runnable pingTask = () -> {
            List<Boolean> isReachablePool = new ArrayList<>();
            int collectingDataCounter = 0;
            while (!IP_ADDRESSES_FOR_DELETE.contains(addedIpAddress)) {
                collectingDataCounter++;
                try {
                    boolean reachable = InetAddress.getByName(addedIpAddress).isReachable(REACHABILITY_CONDITION);
                    isReachablePool.add(reachable);
                    collectingDataCounter = checkHostAttainability(collectingDataCounter, isReachablePool, addedIpAddress);
                    setIPStatusForBot(reachable, addedIpAddress);
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
        try {
            String reachable = InetAddress.getByName(ipAddress).isReachable(REACHABILITY_CONDITION) + "";
            if (reachable.equals("true")) {
                return ipAddress + " is online";
            } else {
                return ipAddress + " is offline";
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    @Override
    public void removeIpAddress(String ipAddress) {
        IP_ADDRESSES_FOR_DELETE.add(ipAddress);
        heartBeatBotService.removeIPStatus(ipAddress);
        logger.info(ipAddress + " was removed from pool");
    }

    private int checkHostAttainability(int collectingDataCounter, List<Boolean> isReachablePool, String ipAddress) throws InterruptedException, IOException {
        if (collectingDataCounter >= COLLECTING_DATA_COUNTER_LIMIT) {
            for (int i = 0; i < isReachablePool.size(); i++) {
                if (!isReachablePool.contains(true)) {
                    heartBeatBotService.sendMessageToBot(ipAddress, "UNREACHABLE!");
                    logger.info(ipAddress + " IS UNREACHABLE!");
                    while (true) {
                        Thread.currentThread().sleep(2000);
                        boolean reachable = InetAddress.getByName(ipAddress).isReachable(REACHABILITY_CONDITION);
                        if (reachable) {
                            heartBeatBotService.sendMessageToBot(ipAddress, "AGAIN+REACHABLE!");
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

    private void setIPStatusForBot(boolean reachable, String ipAddress) {
        if (reachable) {
            heartBeatBotService.setIPStatus(ipAddress, " is online ");
            logger.info("Host " + ipAddress + " is online ");
        } else {
            heartBeatBotService.setIPStatus(ipAddress, " is offline ");
            logger.info("Host " + ipAddress + " is offline ");
        }
    }
}
