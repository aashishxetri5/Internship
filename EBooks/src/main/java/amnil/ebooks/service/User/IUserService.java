package amnil.ebooks.service.User;

import amnil.ebooks.dto.request.ChangePasswordDTO;
import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;
import amnil.ebooks.model.User;

import java.util.List;

public interface IUserService {

    User addUser(UserRequestDTO user);

    ValidUserResponse getUserByEmail(String email);

    List<ValidUserResponse> getAllUsers();

    void deleteUserById(Long userId);

    User updateUser(UserRequestDTO user);

    void updateUserPassword(Long userId, ChangePasswordDTO password);

    Long countUsers();
}
