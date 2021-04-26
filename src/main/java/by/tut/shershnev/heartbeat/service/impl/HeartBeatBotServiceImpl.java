package by.tut.shershnev.heartbeat.service.impl;

import by.tut.shershnev.heartbeat.service.HeartBeatBotService;
import by.tut.shershnev.heartbeat.service.HeartBeatService;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static by.tut.shershnev.heartbeat.service.constant.ServiceConstant.BOT_TOKEN;
import static by.tut.shershnev.heartbeat.service.constant.ServiceConstant.BOT_USERNAME;

@Service
public class HeartBeatBotServiceImpl extends TelegramLongPollingBot implements HeartBeatBotService {

    private final HeartBeatService heartBeatService;
    private static final Logger logger = LogManager.getLogger();
    //private static final Map<String, String> STATUSES = new ConcurrentHashMap<>();
    private static final List<String> CHAT_IDS = new CopyOnWriteArrayList<>();

    public HeartBeatBotServiceImpl(HeartBeatService heartBeatService) {
        this.heartBeatService = heartBeatService;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        CHAT_IDS.add(chatId);
        message.setChatId(chatId);
        String command = update.getMessage().getText();
        switch (command) {
            case "/hello" -> {
                message.setText("Hello!");
            }
            case "/status" -> {
                String statusesForOutput = getIPStatus();
                removeUnnecessarySymbolsAndSetTextToMessage(statusesForOutput, message);
            }
        }
        try {
            if (message.getText() == null) {
                message.setText("Sorry, I dont understand you" + "\n" + "Use" +
                        " valid commands - /status");
            }
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }


    public String getIPStatus() {
        try {
            return heartBeatService.getCurrentStatuses();
        } catch (InterruptedException | IOException e) {
            logger.error(e.getMessage());
        }
        return "";
    }


    @Override
    public void sendMessageToBot(String ipAddress, String message) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        for (String chatId : CHAT_IDS) {
            HttpGet httpget = new HttpGet("https://api.telegram.org/bot" + BOT_TOKEN +
                    "/sendMessage?chat_id=" + chatId + "&text=" + ipAddress + "+" + message);
            httpclient.execute(httpget);
        }

    }

    private void removeUnnecessarySymbolsAndSetTextToMessage(String statuses, SendMessage message) {
        String resultWithoutBrackets = statuses.replaceAll("[{}]", "");
        if (resultWithoutBrackets.isEmpty()) {
            message.setText("There are no added ip addresses");
        } else {
            String resultWithoutEqualSign = resultWithoutBrackets.replaceAll("=", " ");
            message.setText(resultWithoutEqualSign);
        }
    }
}
