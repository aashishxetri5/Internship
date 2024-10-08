package amnil.internship.controller;

import amnil.internship.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {

//        if (request.getSession().getId() == null) {
//            return "redirect:/login";
//        }

        System.out.println(request.getSession().getId());

        model.addAttribute("title", "Home Page");

        Student student = new Student();
        model.addAttribute("student", student);

        List<String> courses = Arrays.asList("BSc. CSIT", "BCA", "BIT");
        model.addAttribute("courses", courses);

        return "index";
    }

}
