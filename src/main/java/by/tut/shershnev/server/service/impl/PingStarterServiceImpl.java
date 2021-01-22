package by.tut.shershnev.server.service.impl;

import by.tut.shershnev.server.service.PingStarterService;
import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;
import org.quartz.JobDetail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static by.tut.shershnev.server.service.impl.PingJobServiceImpl.IP_ADDRESS_KEY;

@Service
public class PingStarterServiceImpl implements PingStarterService {

    @Override
    public void ping(InetAddressDTO inetAddressDTO) throws SchedulerException, IOException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
            String ipAddress = inetAddressDTO.getIpAddress();
            checkIsValidIpAddressAndThrowException(ipAddress);
            List<String> ipAddressesList = new ArrayList<>();
            ipAddressesList.add(ipAddress);

            for (String address : ipAddressesList) {
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("pingTrigger " + address, "group " + address)
                        .startNow()
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(3)
                                .repeatForever())
                        .build();

                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put("pingTrigger", "fireAfterEvery5SecondsRepeatThrice");
                jobDataMap.put(IP_ADDRESS_KEY, address);

                JobDetail jobDetail = JobBuilder.newJob(PingJobServiceImpl.class)
                        .usingJobData(jobDataMap)
                        .withIdentity(address, "group " + address)
                        .build();

                scheduler.scheduleJob(jobDetail, trigger);

            }

        }

        private void checkIsValidIpAddressAndThrowException(String ipAddress) throws IOException{
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            inetAddress.isReachable(100);
        }

}
