package amnil.internship.controller;

import amnil.internship.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User loggedInUser = (User) session.getAttribute("user");
            if (loggedInUser != null) {
                model.addAttribute("user", loggedInUser);
                return "dashboard";
            }
        }
        return "redirect:/login";
    }
}
