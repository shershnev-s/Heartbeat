package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.repository.UserAccountRepository;
import by.tut.shershnev.heartbeat.repository.model.RoleEnum;
import by.tut.shershnev.heartbeat.repository.model.UserAccount;
import by.tut.shershnev.heartbeat.service.HeartBeatService;
import by.tut.shershnev.heartbeat.service.HeartBeatStarterService;
import by.tut.shershnev.heartbeat.service.SerializeDataService;
import by.tut.shershnev.heartbeat.service.exception.IPAddressAlreadyInPoolException;
import by.tut.shershnev.heartbeat.service.model.InetAddressDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
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
    private final UserAccountRepository userAccountRepository;

    public HeartBeatStarterServiceImpl(HeartBeatService HeartBeatService, SerializeDataService serializeDataService, UserAccountRepository userAccountRepository) {
        this.heartBeatService = HeartBeatService;
        this.serializeDataService = serializeDataService;
        this.userAccountRepository = userAccountRepository;
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
/*        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("LolUser");
        userAccount.setRole(RoleEnum.ADMIN);
        userAccount.setPassword("sdfsdfsfsf");
        try {
            userAccountRepository.save(userAccount);
        } catch (DataIntegrityViolationException e){
            logger.info("Attempt to save user with invalid fields " + userAccount.getUsername());
        }*/
    }

    @Override
    public CopyOnWriteArrayList<InetAddressDTO> getAllAddresses() {
        CopyOnWriteArrayList<InetAddressDTO> addresses = new CopyOnWriteArrayList<>();
        for (String ipAddress : ipAddresses) {
            InetAddressDTO inetAddressDTO = new InetAddressDTO();
            inetAddressDTO.setIpAddress(ipAddress);
            inetAddressDTO.setStatus(heartBeatService.doSingleIPCheck(ipAddress));
            addresses.add(inetAddressDTO);
        }
        return addresses;
    }

    @Override
    public void removeIP(InetAddressDTO inetAddressDTO) {
        String removingIP = inetAddressDTO.getIpAddress();
        if (ipAddresses.contains(removingIP)) {
            heartBeatService.removeIpAddress(removingIP);
            ipAddresses.remove(removingIP);
            serializeDataService.serializeIPAddresses(ipAddresses);
        }
        else {
            logger.error("Attempt to remove ip which doesn't exist");
        }
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
