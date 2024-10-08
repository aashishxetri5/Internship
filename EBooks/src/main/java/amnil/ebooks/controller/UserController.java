package amnil.ebooks.controller;

import amnil.ebooks.dto.request.ChangePasswordDTO;
import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        return "redirect:/dashboard/users";
    }

    @PostMapping("/user/change-password")
    public String changePassword(@ModelAttribute ChangePasswordDTO changePasswordDTO, RedirectAttributes redirectAttributes) {

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("message", "Passwords do not match");
            return "redirect:/dashboard/changePassword";
        }

        try {
            userService.updateUserPassword(4L, changePasswordDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard/changePassword";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("profile") UserRequestDTO userRequestDTO, RedirectAttributes redirectAttributes) {

        if (userRequestDTO.getFullname().isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyFullname", "Please enter fullname");
            return "redirect:/dashboard/profile";
        }

        if (userRequestDTO.getAddress().isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyAddress", "Please enter Address");
            return "redirect:/dashboard/profile";
        }

        if (userRequestDTO.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyEmail", "Please enter email");
            return "redirect:/dashboard/profile";
        }

        try {
            userService.updateUser(userRequestDTO);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("updateFailed", ex.getMessage());
        }

        return "redirect:/dashboard/profile";
    }

}
