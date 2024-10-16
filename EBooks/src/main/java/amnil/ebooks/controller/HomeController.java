package amnil.ebooks.controller;

import amnil.ebooks.dto.response.BookResponseDTO;
import amnil.ebooks.service.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HomeController {

    private final BookService bookService;

    @GetMapping("/")
    public String home(Model model) {
        List<BookResponseDTO> allBooks = bookService.getAllBooks();
        model.addAttribute("books", allBooks);
        model.addAttribute("title", "Home Page");
        return "index";
    }
}
