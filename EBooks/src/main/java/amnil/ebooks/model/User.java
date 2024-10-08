package amnil.ebooks.model;

import amnil.ebooks.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private String address;
    private String email;
    private String password;
    private Role role;

    public User(String fullname, String address, String email, String password, Role role) {
        this.fullname = fullname;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
