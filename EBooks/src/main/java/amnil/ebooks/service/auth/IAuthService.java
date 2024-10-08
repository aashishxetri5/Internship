package amnil.ebooks.service.auth;

import amnil.ebooks.dto.request.UserRequestDTO;
import amnil.ebooks.dto.response.ValidUserResponse;

public interface IAuthService {
    ValidUserResponse registerUser(UserRequestDTO userRequestDTO);

}
