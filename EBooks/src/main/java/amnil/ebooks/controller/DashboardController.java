package amnil.ebooks.controller;

import amnil.ebooks.dto.request.BookRequest;
import amnil.ebooks.dto.request.ChangePasswordDTO;
import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.service.User.UserService;
import amnil.ebooks.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DashboardController {

    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/home")
    public String dashboard(Model model) {
        model.addAttribute("totalEbooks", bookService.countTotalBooks());
        model.addAttribute("totalUsers", userService.countUsers());
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ValidUserResponse user = userService.getUserByEmail(email);
        model.addAttribute("profile", user);
        return "dashboardProfile";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("admin", new UserRequestDTO());
        return "dashboardUsers";
    }

    @GetMapping("/genres")
    public String genres() {
        return "dashboard";
    }

    @GetMapping("/ebooks")
    public String ebooks(Model model) {
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute("ebook", new BookRequest());
        return "dashboardBooks";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("changePassword", new ChangePasswordDTO());
        return "changePassword";
    }
}
