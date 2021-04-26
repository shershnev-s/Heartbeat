package by.tut.shershnev.heartbeat.controller.config;

import by.tut.shershnev.heartbeat.service.impl.HeartBeatBotServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Configuration
public class TelegramBotConfig {

    private static final Logger logger = LogManager.getLogger();

    @PostConstruct
    public void start() {
        try {
            logger.info("Instantiate Telegram Bots API...");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            logger.info("Register Telegram Bots API...");
            botsApi.registerBot(new HeartBeatBotServiceImpl(heartBeatService));
        } catch (TelegramApiException e) {
            logger.error("Exception instantiate Telegram Bot!", e);
        }
    }


}
