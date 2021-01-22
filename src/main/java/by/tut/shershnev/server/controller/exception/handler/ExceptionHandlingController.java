package by.tut.shershnev.server.controller.exception.handler;

import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.quartz.ObjectAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(IOException.class)
    public void inetAddressExecuteError(Exception ex){
        System.out.println("i/o error occurred in " + ex.getMessage());
    }

}
