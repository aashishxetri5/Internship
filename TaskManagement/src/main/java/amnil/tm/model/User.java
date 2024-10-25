package amnil.tm.model;

import amnil.tm.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Data
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles; // Roles are stored in separate table but are fetched along with user.

    public User(String fullname, String email, String password, List<Role> roles) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(Long id, String fullname, String email) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
    }
}
