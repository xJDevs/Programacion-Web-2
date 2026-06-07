package teccr.justdoitcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        return doLogout(session);
    }


    private String doLogout(HttpSession session) {
        if (session != null) {
            try {
                session.invalidate();
                log.info("Session invalidated successfully on logout.");
            } catch (IllegalStateException e) {
                log.warn("Session was already invalidated.", e);
            }
        }
        return "redirect:/";
    }
}
