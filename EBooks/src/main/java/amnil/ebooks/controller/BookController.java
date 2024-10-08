package amnil.ebooks.controller;

import amnil.ebooks.dto.request.BookRequest;
import amnil.ebooks.dto.response.BookResponseDTO;
import amnil.ebooks.model.Book;
import amnil.ebooks.service.book.BookService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public String books(Model model) {
        List<BookResponseDTO> allBooks = bookService.getAllBooks();
        model.addAttribute("books", allBooks);
        return "books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload/book")
    public String uploadBook(RedirectAttributes redirectAttributes, @ModelAttribute("ebook") BookRequest request, @RequestParam MultipartFile bookFile) {

        if (bookFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyFile", "Please attach a file");
            return "redirect:/dashboard/ebooks";
        }

        try {
            Book book = bookService.addBook(request, bookFile);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard/ebooks";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBookById(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/ebooks";
    }

    @GetMapping("/book/download/{id}")
    public void downloadBook(@PathVariable Long id, HttpServletResponse res, RedirectAttributes redirectAttributes) {
        try {
            bookService.downloadBook(id, res);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("bookError", e.getMessage());
        }
    }
}
