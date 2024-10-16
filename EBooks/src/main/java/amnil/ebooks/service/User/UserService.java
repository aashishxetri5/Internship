package amnil.ebooks.service.User;

import amnil.ebooks.dto.request.ChangePasswordDTO;
import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.enums.Role;
import amnil.ebooks.exception.DuplicateRecordException;
import amnil.ebooks.exception.NoSuchRecordException;
import amnil.ebooks.exception.OperationFailureException;
import amnil.ebooks.model.User;
import amnil.ebooks.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements IUserService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    @Override
    public User addUser(UserRequestDTO user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new DuplicateRecordException("User already exists");
        }

        return userRepository.save(createUser(user));
    }

    private User createUser(UserRequestDTO request) {
        return new User(
                request.getFullname(),
                request.getAddress(),
                request.getEmail(),
                request.getPassword(),
                Role.USER
        );
    }

    @Override
    public ValidUserResponse getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<ValidUserResponse> getAllUsers() {
        return userRepository.findAllBy();
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new NoSuchRecordException("User not found");
        });
    }

    @Override
    public User updateUser(UserRequestDTO user) {
        return userRepository.findById(user.getId())
                .map(existingUser -> updateExistingUser(existingUser, user))
                .map(userRepository::save)
                .orElseThrow(() -> new OperationFailureException("Failed to update user"));
    }

    private User updateExistingUser(User existingUser, UserRequestDTO user) {
        existingUser.setFullname(user.getFullname());
        existingUser.setAddress(user.getAddress());
        existingUser.setEmail(user.getEmail());

        return existingUser;
    }

    @Override
    public void updateUserPassword(Long userId, ChangePasswordDTO password) {
        userRepository.findById(userId).ifPresentOrElse(
                user -> {
                    if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
                        throw new OperationFailureException("Old password does not match");
                    }
                    user.setPassword(passwordEncoder.encode(password.getNewPassword()));
                    userRepository.save(user);
                },
                () -> {
                    throw new NoSuchRecordException("User not found");
                }
        );
    }

    @Override
    public Long countUsers() {
        return userRepository.count();
    }
}
