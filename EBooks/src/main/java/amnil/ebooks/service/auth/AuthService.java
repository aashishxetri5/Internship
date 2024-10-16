package amnil.ebooks.service.auth;

import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.exception.DuplicateRecordException;
import amnil.ebooks.exception.NoSuchRecordException;
import amnil.ebooks.model.User;
import amnil.ebooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService implements IAuthService, UserDetailsService {

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchRecordException("User Not Found for email")
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

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
        return new ValidUserResponse(user.getId(), user.getFullname(), user.getAddress(), user.getEmail(), user.getRole());
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

    public UserDetails validateUserLogin(UserRequestDTO userRequestDTO) {
        try {
            UserDetails userDetails = loadUserByUsername(userRequestDTO.getEmail());

            if (!passwordEncoder.matches(userRequestDTO.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            System.out.println(userDetails);
            return userDetails;
        } catch (NoSuchRecordException e) {
            throw new NoSuchRecordException(e.getMessage());
        }
    }

}
