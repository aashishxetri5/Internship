package amnil.tm.service.user;

import amnil.tm.dto.request.AddUserRequest;
import amnil.tm.dto.response.UserResponse;
import amnil.tm.exception.DuplicateRecordException;
import amnil.tm.model.User;
import amnil.tm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse addUser(AddUserRequest request) {

        User user = convertToUser(request);

        return Optional.of(user)
                .filter(
                        existingUser -> !userRepository.existsByEmail(user.getEmail())
                )
                .map(userRepository::save)
                .map(this::convertToResponse)
                .orElseThrow(
                        () -> new DuplicateRecordException("User already exists!!")
                );
    }

    private User convertToUser(AddUserRequest request) {
        return new User(
                request.getFullname(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRoles()
        );
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullname(),
                user.getEmail(),
                user.getRoles()
        );
    }

    @Override
    public List<UserResponse> getUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

//    @Override
//    public UserResponse getUserById(Long id) {
//        return userRepository.findById(id)
//                .map(this::convertToResponse)
//                .orElseThrow(
//                        () -> new NoSuchElementException("User does not exist!!")
//                );
//    }
//
//    @Override
//    public void deleteUser(Long id) {
//        userRepository.findById(id).ifPresentOrElse(
//                userRepository::delete,
//                () -> {
//                    throw new NoSuchElementException("User does not exist!!");
//                }
//        );
//    }
//
//    @Override
//    public UserResponse updateUser(User user) {
//        return userRepository.findById(user.getId())
//                .map(userRepository::save)
//                .map(this::convertToResponse)
//                .orElseThrow(
//                        () -> new NoSuchElementException("User does not exist!!")
//                );
//    }
}
