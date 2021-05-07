package by.tut.shershnev.heartbeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogonController {

    @GetMapping("/")
    public String forwardToMainPage(){
        return "redirect:/add_ip";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login";
    }
}
