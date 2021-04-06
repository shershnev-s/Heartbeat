package by.tut.shershnev.heartbeat.controller;

import by.tut.shershnev.heartbeat.service.HeartBeatStarterService;
import by.tut.shershnev.heartbeat.service.exception.IPAddressAlreadyInPoolException;
import by.tut.shershnev.heartbeat.service.model.InetAddressDTO;
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
@RequestMapping("/add_ip")
public class AddIPController {

    private final HeartBeatStarterService heartBeatStarterService;

    public AddIPController(HeartBeatStarterService heartBeatStarterService) {

        this.heartBeatStarterService = heartBeatStarterService;
    }

    @PostMapping
    public String setIP(@Valid @ModelAttribute(name = "inetAddress") InetAddressDTO inetAddress,
                        BindingResult errors, final RedirectAttributes redirectAttributes,
                        Model model) throws InterruptedException, ExecutionException {
        if (errors.hasErrors()) {
            model.addAttribute("inetAddress", inetAddress);
            return "add_addresses";
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
    public String getAdmin(@ModelAttribute(name = "exception") String exception, Model model) throws InterruptedException {
        CopyOnWriteArrayList<InetAddressDTO> addresses = heartBeatStarterService.getAllAddresses();
        model.addAttribute("inetAddress", new InetAddressDTO());
        model.addAttribute("exception", exception);
        model.addAttribute("addresses", addresses);
        return "add_addresses";
    }

    @GetMapping("/delete_ip/{ip}")
    public String deleteIP(@PathVariable(value = "ip") String ip) {
        InetAddressDTO inetAddressDTO = new InetAddressDTO();
        inetAddressDTO.setIpAddress(ip);
        heartBeatStarterService.removeIP(inetAddressDTO);
        return "redirect:/add_ip";
    }
}
