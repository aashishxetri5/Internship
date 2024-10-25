package amnil.tm.dto.response;

import amnil.tm.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String fullname;

    private String email;

    private List<Role> roles;


}
