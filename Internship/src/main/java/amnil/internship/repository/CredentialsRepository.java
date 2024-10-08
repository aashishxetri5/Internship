package amnil.internship.repository;

import amnil.internship.model.Credentials;
import amnil.internship.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Credentials findByUsernameAndPassword(String username, String password);

    Credentials findByUsername(String username);
}
