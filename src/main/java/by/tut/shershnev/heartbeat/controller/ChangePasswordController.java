package by.tut.shershnev.heartbeat.controller;

import by.tut.shershnev.heartbeat.service.UserService;
import by.tut.shershnev.heartbeat.service.model.HBUser;
import by.tut.shershnev.heartbeat.service.model.UserDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ChangePasswordController {

    private final UserService userService;

    public ChangePasswordController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/change_password")
    public String getChangePassword(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("message", "");
        return "change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(
            @ModelAttribute(name = "user") UserDTO userDTO, Model model
    ) {
        String result = userService.changePassword(userDTO);
        model.addAttribute("message", result);
        return "change_password";
    }
}
