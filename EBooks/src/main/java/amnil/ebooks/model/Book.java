package amnil.ebooks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private String thumbnailPath;
    private String filePath;


    public Book(String title, String description, String author, String publisher, String thumbnailPath, String filePath) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.thumbnailPath = thumbnailPath;
        this.filePath = filePath;
    }
}
