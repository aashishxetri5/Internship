package amnil.internship.controller;

import amnil.internship.dto.request.LoginRequest;
import amnil.internship.dto.response.UserDataResponse;
import amnil.internship.exception.RecordNotFoundException;
import amnil.internship.model.Credentials;
import amnil.internship.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    public final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        Credentials credentials = new Credentials();
        model.addAttribute("credentials", credentials);
        return "login";
    }

    @PostMapping("/user/login")
    public String login(RedirectAttributes redirectAttributes, @RequestParam String username, @RequestParam String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);

        try {
            UserDataResponse loggedInUser = authService.login(loginRequest);

            if (loggedInUser != null) {
                System.out.println("Login Successful\n" + loggedInUser);
                redirectAttributes.addFlashAttribute("user", loggedInUser);
                return "redirect:/";
            }

        } catch (RecordNotFoundException ex) {
            System.out.println("Login Failed\n");

            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }
}
