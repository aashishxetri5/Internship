package amnil.internship.controller;

import amnil.internship.dto.request.UpdateStudentRequest;
import amnil.internship.enums.Role;
import amnil.internship.model.Address;
import amnil.internship.model.Credentials;
import amnil.internship.model.Student;
import amnil.internship.model.User;
import amnil.internship.service.student.IStudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class StudentController {

    public final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("saveData")
    public String collectStudentDetails(RedirectAttributes redirectAttributes, @ModelAttribute("student") Student student) {

//        Address address = new Address("Kathmandu", "Bagmati", "Nepal", "44600");
//        Credentials credentials = new Credentials("aashish", "ASDFGH123");
//        User user = new User("Aashish Katwal", "aashish@gmail.com", Role.USER, "9863468219", credentials, address);
//        Student student1 = new Student(user, 211812L, 7, LocalDate.now(), "CSIT");

//        Student registeredStudent = studentService.registerStudent(student1);

//        if (registeredStudent != null)
//            redirectAttributes.addFlashAttribute("message", "Student Registered Successfully");
//        else
//            redirectAttributes.addFlashAttribute("message", "Failed to Register Student");
        return "redirect:/";
    }

    @GetMapping("viewData")
    public String displayAllStudentRecords(Model model) {
        List<Student> allStudents = studentService.getAllStudent();
        model.addAttribute("students", allStudents);

        return "viewData";
    }

    @GetMapping("delete/{id}")
    public String deleteStudentRecord(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("message", "Deleted Record Successfully");
        return "redirect:/viewData";
    }

    @GetMapping("update/{id}")
    public String fetchStudentRecord(Model model, @PathVariable Long id) {

        Student student = studentService.getStudentById(id);
        model.addAttribute("studentRecord", student);
        System.out.println(student.toString());

        List<String> courses = Arrays.asList("BSc. CSIT", "BCA", "BIT");
        model.addAttribute("courses", courses);

        return "updateData";
    }

    @PostMapping("update")
    public String updateStudentRecord(RedirectAttributes redirectAttributes, @ModelAttribute("student") UpdateStudentRequest newStudentData, @RequestParam Long id) {

        Student student = studentService.updateStudent(newStudentData, id);

        if (student != null)
            redirectAttributes.addFlashAttribute("message", "Student Record Updated Successfully");
        else
            redirectAttributes.addFlashAttribute("message", "Failed to Update Student Record");

        return "redirect:/viewData";
    }

}
