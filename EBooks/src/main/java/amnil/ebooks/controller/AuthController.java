package amnil.ebooks.controller;

import amnil.ebooks.dto.request.LoginRequest;
import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.enums.Role;
import amnil.ebooks.exception.DuplicateRecordException;
import amnil.ebooks.exception.NoSuchRecordException;
import amnil.ebooks.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/auth/login")
    public String login(Model model) {
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("userLogin", loginRequest);
        return "login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/auth/signup")
    public String signup(Model model) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        model.addAttribute("userSignup", userRequestDTO);
        return "signup";
    }

    @PostMapping("/auth/signup")
    public String processSignup(@ModelAttribute("userSignup") UserRequestDTO request, RedirectAttributes redirectAttributes) {

        request.setRole(Role.USER);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("passwordDoNotMatch", "Passwords do not match");
            return "redirect:/signup";
        }

        try {
            ValidUserResponse response = authService.registerUser(request);

            if (response != null) {
                redirectAttributes.addFlashAttribute("userRegistered", "Registration Successful");
                return "redirect:/login";
            }
        } catch (DuplicateRecordException e) {
            redirectAttributes.addFlashAttribute("duplicateRecord", e.getMessage());
        }
        return "redirect:/signup";
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(@ModelAttribute("admin") UserRequestDTO admin, RedirectAttributes redirectAttributes) {
        admin.setRole(Role.ADMIN);

        if (!(admin.getPassword().equals(admin.getConfirmPassword()))) {
            redirectAttributes.addFlashAttribute("passwordDoNotMatch", "Passwords do not match");
            return "redirect:/dashboard/users";
        }

        try {
            authService.registerUser(admin);
        } catch (DuplicateRecordException e) {
            redirectAttributes.addFlashAttribute("duplicateRecord", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("duplicateRecord", "Something went wrong");
        }

        return "redirect:/dashboard/users";
    }

    @PostMapping("/auth/login")
    public String authenticateUser(@ModelAttribute UserRequestDTO loginRequest,
                                   RedirectAttributes redirectAttributes, HttpServletRequest request) {

        try {
            UserDetails details = authService.validateUserLogin(loginRequest);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    details, loginRequest.getPassword(), details.getAuthorities()
            );

            Authentication authResult = authenticationManager.authenticate(authToken);

            if (authResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            }

        } catch (NoSuchRecordException e) {
            redirectAttributes.addFlashAttribute("emailError", e.getMessage());
            return "redirect:/auth/login";
        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("passwordError", e.getMessage());
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("loginFailed", "Something went wrong. Try Again!");
            return "redirect:/auth/login";
        }

        return "redirect:/";
    }

}
