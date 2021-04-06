package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.service.SerializeDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SerializeDataServiceImpl implements SerializeDataService {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void serializeIPAddresses(List<String> ipAddresses) {
        String path = "./ipAddresses.obj";
        try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeObject(ipAddresses);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
