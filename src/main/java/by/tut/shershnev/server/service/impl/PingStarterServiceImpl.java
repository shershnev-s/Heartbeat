package by.tut.shershnev.server.service.impl;

import by.tut.shershnev.server.service.PingStarterService;
import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PingStarterServiceImpl implements PingStarterService {

    private final PingServiceImpl pingService;
    public static final String IP_ADDRESS_KEY = "ip_addr";

    public PingStarterServiceImpl(PingServiceImpl pingService) {
        this.pingService = pingService;
    }

    public void startPing(InetAddressDTO inetAddressDTO) throws IOException, InterruptedException {
        String ipAddress = inetAddressDTO.getIpAddress();
        checkIsValidIpAddress(ipAddress);
        pingService.ping(ipAddress);
    }

    private void checkIsValidIpAddress(String ipAddress) throws IOException {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        boolean isValid = ipAddress.matches(PATTERN);
        if (!isValid){
            throw new IOException();
        }
    }

}
