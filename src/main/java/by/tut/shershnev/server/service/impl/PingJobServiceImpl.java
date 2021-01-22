package by.tut.shershnev.server.service.impl;

import by.tut.shershnev.server.service.PingJobService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PingJobServiceImpl implements PingJobService, Job {

    public static final String IP_ADDRESS_KEY = "ip_addr";

    //private final Test test = new Test();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        String ipAddr = (String) jobDataMap.get(IP_ADDRESS_KEY);
        try {

            InetAddress inetAddress = InetAddress.getByName(ipAddr);
            boolean reachable = inetAddress.isReachable(100);

            if (reachable) {
                System.out.println(inetAddress.getHostAddress() + " is online " + Thread.currentThread().getName());
            } else {
                System.out.println(inetAddress.getHostAddress() + " is offline " + Thread.currentThread().getName());
            }
            System.out.println("---------------------------------------------");
            String str = reachable + " IP address " + ipAddr;

            List<String> ipStatuses = new ArrayList<>();
            Test test = new Test();
                    test.addToPing(str, ipStatuses);

        }   catch (UnknownHostException e){
            System.out.println("User sent invalid ip" + e.getMessage());
        }   catch (IOException | IllegalArgumentException e){
            System.out.println("Network error occurs " + e.getMessage() );
        }
    }

}
