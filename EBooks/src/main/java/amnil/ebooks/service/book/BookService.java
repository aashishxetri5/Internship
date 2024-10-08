package amnil.ebooks.service.book;

import amnil.ebooks.dto.request.BookRequest;
import amnil.ebooks.dto.response.BookResponseDTO;
import amnil.ebooks.exception.NoSuchRecordException;
import amnil.ebooks.exception.OperationFailureException;
import amnil.ebooks.exception.ResourceNotFoundException;
import amnil.ebooks.model.Book;
import amnil.ebooks.repository.BookRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final ConversionService conversionService;

    @Override
    public Book addBook(BookRequest request, MultipartFile bookFile) {
        String baseDir = System.getProperty("user.dir");
        String dirPath = baseDir + File.separator + "uploads" + File.separator + "file";
        String path = dirPath + File.separator + bookFile.getOriginalFilename();

        File directory = new File(dirPath);

        if (!directory.exists()) {
            boolean mkdirs = directory.mkdirs();
            if (!mkdirs) {
                throw new OperationFailureException("Failed to save file at: " + directory.getAbsolutePath());
            }
        }

        try {
            System.out.println(path);
            bookFile.transferTo(new File(path));
            return bookRepository.save(createBook(request, path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationFailureException("Failed to save file");
        }
    }

    private Book createBook(BookRequest request, String path) {
        System.out.println("Path " + path );
        return new Book(
                request.getTitle(),
                request.getDescription(),
                request.getAuthor(),
                request.getPublisher(),
                request.getThumbnailPath(),
                path
        );
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new NoSuchRecordException("Book not found")
        );
    }

    @Override
    public String getBookLocationById(Long bookId) {
        return getBookById(bookId).getFilePath();
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAllBy();
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepository.findById(bookId).ifPresentOrElse(bookRepository::delete, () -> {
            throw new NoSuchRecordException("Book does not exist");
        });
    }

    @Override
    public Book updateBook(BookRequest request, Long bookId) {
        return bookRepository.findById(bookId)
                .map(existingBook -> updateExistingBook(existingBook, request))
                .map(bookRepository::save)
                .orElseThrow(() -> new OperationFailureException("Failed to update book"));
    }

    private Book updateExistingBook(Book existingBook, BookRequest newBookData) {
        existingBook.setTitle(newBookData.getTitle());
        existingBook.setDescription(newBookData.getDescription());
        existingBook.setAuthor(newBookData.getAuthor());
        existingBook.setPublisher(newBookData.getPublisher());

        return existingBook;
    }

    @Override
    public Long countTotalBooks() {
        return bookRepository.count();
    }

    @Override
    public void downloadBook(Long id, HttpServletResponse res) {
        try {
            String bookPath = getBookLocationById(id);
            Path path = Path.of(bookPath).normalize();

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new ResourceNotFoundException("Requested file doesn't exist!");
            }

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            res.setContentType(contentType);
            res.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            res.setContentLength((int) Files.size(path));

            try (InputStream inputStream = Files.newInputStream(path)) {
                inputStream.transferTo(res.getOutputStream());
                res.flushBuffer();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FAILED TO DOWNLOAD FILES");
            throw new OperationFailureException("Failed to download file");
        }
    }
}
