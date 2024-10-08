package amnil.internship.service.auth;

import amnil.internship.dto.request.LoginRequest;
import amnil.internship.dto.response.UserDataResponse;
import amnil.internship.model.User;
import amnil.internship.model.User;

public interface IAuthService {
    UserDataResponse login(LoginRequest request);

}
