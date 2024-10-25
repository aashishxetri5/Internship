package amnil.tm.service.user;

import amnil.tm.dto.request.AddUserRequest;
import amnil.tm.dto.response.UserResponse;
import amnil.tm.model.User;

import java.util.List;

public interface IUserService {

    UserResponse addUser(AddUserRequest request);

    List<UserResponse> getUsers();

//    UserResponse getUserById(Long id);
//
//    void deleteUser(Long id);
//
//    UserResponse updateUser(User user);

}
