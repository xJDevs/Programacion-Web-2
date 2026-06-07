package teccr.justdoitcloud.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.service.UserService;

import java.util.Optional;

@Slf4j
@Controller
public class LoginController {

    private final UserService userService;

    // Constructor injection of UserService
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, HttpSession session) {
        log.info("Login attempt for user: {}", username);

        Optional<User> userOpt = userService.authenticate(username);
        if (userOpt.isPresent()) {
            log.info("User authenticated successfully: {}", username);
            // Save user in session
            session.setAttribute("user", userOpt.get());
            return "redirect:/user/home";
        } else {
            log.warn("Authentication failed for user: {}", username);
            return "redirect:/?error=invalid_user";
        }
    }
}
