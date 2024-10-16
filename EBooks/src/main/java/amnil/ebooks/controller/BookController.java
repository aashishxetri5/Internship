package amnil.ebooks.controller;

import amnil.ebooks.dto.request.BookRequest;
import amnil.ebooks.dto.response.BookEditDTO;
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

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public String uploadBook(RedirectAttributes redirectAttributes, @ModelAttribute("ebook") BookRequest request, @RequestParam MultipartFile bookFile) {

        if (bookFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyFile", "Please attach a file");
            return "redirect:/dashboard/ebooks";
        }

        try {
            bookService.addBook(request, bookFile);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard/ebooks";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBookById(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/ebooks";
    }

    @GetMapping("/download/{id}")
    public void downloadBook(@PathVariable Long id, HttpServletResponse res, RedirectAttributes redirectAttributes) {
        try {
            bookService.downloadBook(id, res);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("bookError", e.getMessage());
        }
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);

        BookEditDTO bookResponse = new BookEditDTO(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthor(),
                book.getPublisher()
        );

        model.addAttribute("ebook", bookResponse);

        return "dashboardEditBooks";
    }

    @PostMapping("/edit")
    public String saveEditedBookDetails(@ModelAttribute("ebook") BookEditDTO bookEditDTO, RedirectAttributes redirectAttributes) {
        try {
            bookService.updateBook(bookEditDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/ebooks";
    }
}
