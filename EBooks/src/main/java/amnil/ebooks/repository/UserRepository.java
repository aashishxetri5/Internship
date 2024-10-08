package amnil.ebooks.repository;

import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<ValidUserResponse> findAllBy();

    ValidUserResponse findUserById(Long id);

    boolean getUserByEmail(String email);

    ValidUserResponse findUserByEmail(String email);
}
