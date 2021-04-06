package by.tut.shershnev.heartbeat.controller.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger logger = LogManager.getLogger();

    @ExceptionHandler(IOException.class)
    public void inetAddressExecuteError(Exception e){
        logger.error(e.getMessage());
    }

}
