package amnil.ebooks.service.auth;

import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.exception.DuplicateRecordException;
import amnil.ebooks.exception.NoSuchRecordException;
import amnil.ebooks.model.CustomUserDetails;
import amnil.ebooks.model.User;
import amnil.ebooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService implements IAuthService, UserDetailsService {

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Email: " + email);
        System.out.println("User: " + userRepository.findByEmail(email));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchRecordException("User Not Found for email: " + email));


        return new CustomUserDetails(
                user.getId(),
                user.getFullname(),
                user.getAddress(),
                user.getEmail(),
                user.getPassword(),
                List.of(() -> user.getRole().name())
        );
    }

    @Override
    public ValidUserResponse registerUser(UserRequestDTO userRequestDTO) {
        Optional<User> userData = userRepository.findByEmail(userRequestDTO.getEmail());

        if (userData.isPresent()) {
            throw new DuplicateRecordException("User Already Exists");
        }

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        User user = userRepository.save(createUser(userRequestDTO));

        return createUserResponse(user);
    }

    private ValidUserResponse createUserResponse(User user) {
        return new ValidUserResponse(
                user.getId(),
                user.getFullname(),
                user.getAddress(),
                user.getEmail(),
                user.getRole()
        );
    }

    private User createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setFullname(userRequestDTO.getFullname());
        user.setAddress(userRequestDTO.getAddress());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        return user;
    }

}
