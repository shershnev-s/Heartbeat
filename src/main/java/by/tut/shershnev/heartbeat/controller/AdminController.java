package by.tut.shershnev.heartbeat.controller;

import by.tut.shershnev.heartbeat.service.HeartBeatStarterService;
import by.tut.shershnev.heartbeat.service.exception.IPAddressAlreadyInPoolException;
import by.tut.shershnev.heartbeat.service.model.InetAddressDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

@Controller
@SessionAttributes("username")
@RequestMapping("/add_ip")
public class AdminController {

    private final HeartBeatStarterService heartBeatStarterService;
    private static final Logger logger = LogManager.getLogger();

    public AdminController(HeartBeatStarterService heartBeatStarterService) {

        this.heartBeatStarterService = heartBeatStarterService;
    }

    @ModelAttribute("username")
    public String getUserName() {
        return "";
    }

    @PostMapping
    public String setIP(@Valid @ModelAttribute(name = "inetAddress") InetAddressDTO inetAddress,
                        BindingResult errors, final RedirectAttributes redirectAttributes,
                        Model model) throws InterruptedException, ExecutionException {
        if (errors.hasErrors()) {
            model.addAttribute("inetAddress", inetAddress);
            return "index";
        } else {
            try {
                heartBeatStarterService.startIPAttainabilityChecking(inetAddress);
                return "redirect:/add_ip";
            } catch (IPAddressAlreadyInPoolException | IOException e) {
                redirectAttributes.addFlashAttribute("exception", e.getMessage());
                return "redirect:/add_ip";
            }
        }
    }

    @GetMapping
    public String getAdmin(@ModelAttribute(name = "exception") String exception, Model model) {

        CopyOnWriteArrayList<InetAddressDTO> addresses = null;
        try {
            addresses = heartBeatStarterService.getAllAddresses();
        } catch (InterruptedException | IOException e) {
            model.addAttribute("exception", "something gone wrong");
            logger.error(e.getMessage());
        }
        model.addAttribute("inetAddress", new InetAddressDTO());
        model.addAttribute("exception", exception);
        model.addAttribute("addresses", addresses);
        return "index";
    }

    @GetMapping("/delete_ip/{ip}")
    public String deleteIP(@PathVariable(value = "ip") String ip) {
        InetAddressDTO inetAddressDTO = new InetAddressDTO();
        inetAddressDTO.setIpAddress(ip);
        heartBeatStarterService.removeIP(inetAddressDTO);
        return "redirect:/add_ip";
    }
}
