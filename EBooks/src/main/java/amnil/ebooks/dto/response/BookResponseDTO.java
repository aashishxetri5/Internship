package amnil.ebooks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@ToString
public class BookResponseDTO {
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @NotNull
    private String thumbnailPath;

    @NotNull
    private String filePath;

}
