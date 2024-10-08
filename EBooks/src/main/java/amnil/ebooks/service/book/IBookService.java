package amnil.ebooks.service.book;

import amnil.ebooks.dto.request.BookRequest;
import amnil.ebooks.dto.response.BookResponseDTO;
import amnil.ebooks.model.Book;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookService {
    Book addBook(BookRequest book, MultipartFile bookFile);

    Book getBookById(Long bookId);

    String getBookLocationById(Long bookId);

    List<BookResponseDTO> getAllBooks();

    void deleteBookById(Long bookId);

    Book updateBook(BookRequest book, Long bookId);

    Long countTotalBooks();

    void downloadBook(Long id, HttpServletResponse res);
}
