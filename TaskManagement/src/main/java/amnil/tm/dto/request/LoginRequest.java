package amnil.tm.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class LoginRequest {
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

}
