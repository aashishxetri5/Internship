package amnil.tm.dto.request;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

}
