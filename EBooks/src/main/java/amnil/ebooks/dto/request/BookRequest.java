package amnil.ebooks.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    private Long bookId;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String author;

    @NotNull
    @NotBlank
    private String publisher;

    private String thumbnailPath = "/images/default-thumbnail.png";
}
