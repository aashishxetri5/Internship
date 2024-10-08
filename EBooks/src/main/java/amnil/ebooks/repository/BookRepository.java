package amnil.ebooks.repository;

import amnil.ebooks.dto.response.BookResponseDTO;
import amnil.ebooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookResponseDTO> findAllBy();
}
