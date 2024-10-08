package amnil.ebooks.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@ToString
public class LoginRequest {
    @NotNull(message = "Email cannot be empty")
    @NotBlank
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "Password field cannot be empty")
    @NotBlank
    private String password;
}
