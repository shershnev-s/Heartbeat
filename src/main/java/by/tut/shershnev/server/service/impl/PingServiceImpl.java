package by.tut.shershnev.server.service.impl;

import by.tut.shershnev.server.service.PingService;
import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Service
public class PingServiceImpl implements PingService {

    public void ping(String ipAddress) throws InterruptedException {
        Runnable pingTask = () -> {
            while (true) {
                System.out.println(Thread.currentThread().getName());
                boolean reachable = false;
                try {
                    reachable = InetAddress.getByName(ipAddress).isReachable(800);
                } catch (IOException e) {
                    System.out.println("Validation didn't work");
                }
                System.out.println("Host " + ipAddress + " is " + reachable);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
           }
        };
        Thread thread = new Thread(pingTask);
        thread.start();
    }
}
