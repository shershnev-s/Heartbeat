package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.repository.UserRepository;
import by.tut.shershnev.heartbeat.service.HeartBeatService;
import by.tut.shershnev.heartbeat.service.HeartBeatStarterService;
import by.tut.shershnev.heartbeat.service.SerializeDataService;
import by.tut.shershnev.heartbeat.service.exception.IPAddressAlreadyInPoolException;
import by.tut.shershnev.heartbeat.service.model.InetAddressDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import static by.tut.shershnev.heartbeat.service.constant.ServiceConstant.IP_PATTERN;
import static by.tut.shershnev.heartbeat.service.constant.ServiceConstant.PATH_TO_SAVED_IP_ADDRESSES;

@Service
public class HeartBeatStarterServiceImpl implements HeartBeatStarterService {

    private final HeartBeatService heartBeatService;
    private final SerializeDataService serializeDataService;
    private static final Logger logger = LogManager.getLogger();
    private CopyOnWriteArrayList<String> ipAddresses = new CopyOnWriteArrayList<>();
    private final UserRepository userRepository;

    public HeartBeatStarterServiceImpl(HeartBeatService HeartBeatService, SerializeDataService serializeDataService, UserRepository userRepository) {
        this.heartBeatService = HeartBeatService;
        this.serializeDataService = serializeDataService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void deserializeIPAddresses() {
        if (new File(PATH_TO_SAVED_IP_ADDRESSES).exists()) {
            try (final FileInputStream fileOutputStream = new FileInputStream(PATH_TO_SAVED_IP_ADDRESSES);
                 final ObjectInputStream objectInputStream = new ObjectInputStream(fileOutputStream)) {
                ipAddresses = (CopyOnWriteArrayList<String>) objectInputStream.readObject();
                for (String element : ipAddresses) {
                    heartBeatService.startIPAttainabilityCheckingThread(element);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException | ExecutionException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Transactional
    @Override
    public void startIPAttainabilityChecking(InetAddressDTO inetAddressDTO) throws IOException, InterruptedException, ExecutionException {
        String ipAddress = inetAddressDTO.getIpAddress();
        checkValidityIpAddress(ipAddress);
        ipAddresses.add(ipAddress);
        serializeDataService.serializeIPAddresses(ipAddresses);
        heartBeatService.startIPAttainabilityCheckingThread(ipAddress);
    }

    @Override
    public CopyOnWriteArrayList<InetAddressDTO> getAllAddresses() throws IOException, InterruptedException {
        CopyOnWriteArrayList<InetAddressDTO> addresses = new CopyOnWriteArrayList<>();
        for (String ipAddress : ipAddresses) {
            InetAddressDTO inetAddressDTO = new InetAddressDTO();
            inetAddressDTO.setIpAddress(ipAddress);
            inetAddressDTO.setStatus(heartBeatService.getStatusForIPAddress(ipAddress));
            addresses.add(inetAddressDTO);
        }
        return addresses;
    }

    @Override
    public void removeIP(InetAddressDTO inetAddressDTO) {
        String removingIP = inetAddressDTO.getIpAddress();
        if (ipAddresses.contains(removingIP)) {
            heartBeatService.removeIPAddress(removingIP);
            ipAddresses.remove(removingIP);
            serializeDataService.serializeIPAddresses(ipAddresses);
        }
        else {
            logger.error("Attempt to remove ip which doesn't exist");
        }
    }

    @Override
    public List<String> getCurrentCheckedIPAddresses() {
        return ipAddresses;
    }

    private void checkValidityIpAddress(String ipAddress) throws IPAddressAlreadyInPoolException, IOException {
        boolean isValid = ipAddress.matches(IP_PATTERN);
        if (!isValid) {
            throw new IOException("Invalid IP, please insert another one");
        }
        if (ipAddresses.contains(ipAddress)) {
            throw new IPAddressAlreadyInPoolException("IP already in pool, please insert another one");
        }
    }
}
