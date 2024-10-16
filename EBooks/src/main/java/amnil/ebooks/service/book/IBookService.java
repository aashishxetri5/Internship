package amnil.ebooks.service.book;

import amnil.ebooks.dto.request.BookRequest;
import amnil.ebooks.dto.response.BookEditDTO;
import amnil.ebooks.dto.response.BookResponseDTO;
import amnil.ebooks.model.Book;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookService {
    void addBook(BookRequest book, MultipartFile bookFile);

    Book getBookById(Long bookId);

    String getBookLocationById(Long bookId);

    List<BookResponseDTO> getAllBooks();

    void deleteBookById(Long bookId);

    void updateBook(BookEditDTO book);

    Long countTotalBooks();

    void downloadBook(Long id, HttpServletResponse res);
}
