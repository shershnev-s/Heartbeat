package by.tut.shershnev.server.controller;

import by.tut.shershnev.server.service.PingStarterService;
import by.tut.shershnev.server.service.PingService;
import by.tut.shershnev.server.service.model.InetAddressDTO;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AdminController {

    private final PingStarterService pingStarterService;

    public AdminController(PingStarterService pingStarterService) {

        this.pingStarterService = pingStarterService;
    }

    @PostMapping("/set_ip")
    public String setIP(@Valid @ModelAttribute(name = "inetAddress") InetAddressDTO inetAddress, BindingResult errors, Model model) throws InterruptedException {
        if (errors.hasErrors()) {
            model.addAttribute("inetAddress", inetAddress);
            return "admin";
        } else {
            try {
                pingStarterService.startPing(inetAddress);
                return "redirect:/admin";
            } catch (IOException e){
                model.addAttribute("exception", "This ip is invalid");
                return "admin";
            }
        }
    }

    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("inetAddress", new InetAddressDTO());
        model.addAttribute("exception", "");
        return "admin";
    }

}
