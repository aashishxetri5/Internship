package amnil.internship.model;

import amnil.internship.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private String email;
    private Role role;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private Credentials credentials;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private Address address;

    public User(String fullname, String email, Role role, String phoneNumber, Credentials credentials, Address address) {
        this.fullname = fullname;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.credentials = credentials;
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", credentials=" + credentials +
                ", address=" + address +
                '}';
    }
}
