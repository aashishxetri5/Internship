package amnil.ebooks.dto.response;

import amnil.ebooks.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@ToString
public class ValidUserResponse {

    @NotNull
    private Long id;

    @NotNull
    private String address;

    @NotNull
    private String fullname;

    @NotNull
    private String email;

    @NotNull
    private Role role;
}
