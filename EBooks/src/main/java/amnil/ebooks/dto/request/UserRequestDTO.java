package amnil.ebooks.dto.request;

import amnil.ebooks.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@ToString
public class UserRequestDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String fullname;

    @NotNull
    @NotBlank
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;

    @NotNull
    @NotBlank
    private Role role;
}
