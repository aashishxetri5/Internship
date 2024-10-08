package amnil.internship.service.auth;

import amnil.internship.dto.request.LoginRequest;
import amnil.internship.dto.response.UserDataResponse;
import amnil.internship.exception.RecordNotFoundException;
import amnil.internship.model.Credentials;
import amnil.internship.model.Student;
import amnil.internship.repository.CredentialsRepository;
import amnil.internship.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService implements IAuthService {

    public final StudentRepository studentRepository;
    public final CredentialsRepository credentialsRepository;
    public final PasswordEncoder passwordEncoder;

    @Override
    public UserDataResponse login(LoginRequest request) {
        Credentials credentials = credentialsRepository.findByUsername(request.getUsername());

        if (credentials == null) {
            throw new RecordNotFoundException("Record not found");
        }

        if (passwordEncoder.matches(credentials.getPassword(), request.getPassword())) {
            System.out.println("Valid login");
        }

        Student student = studentRepository.findById(credentials.getId()).orElseThrow(
                () -> new RecordNotFoundException("Record not found")
        );

        return populateDataForUser(student);
    }

    /**
     * @param student Student data upon successful login
     * @return userDTO
     */
    public UserDataResponse populateDataForUser(Student student) {
        UserDataResponse userDataResponse = new UserDataResponse();

//      Basic Details
        userDataResponse.setId(student.getId());
        userDataResponse.setFullname(student.getUser().getFullname());
        userDataResponse.setEmail(student.getUser().getEmail());
        userDataResponse.setPhoneNumber(student.getUser().getPhoneNumber());
        userDataResponse.setRole(student.getUser().getRole());
        userDataResponse.setSemester(student.getSemester());

//      Set Username
        userDataResponse.setUsername(student.getUser().getCredentials().getUsername());

//      Set Address
        userDataResponse.setState(student.getUser().getAddress().getState());
        userDataResponse.setCity(student.getUser().getAddress().getCity());
        userDataResponse.setCountry(student.getUser().getAddress().getCountry());
        userDataResponse.setZip(student.getUser().getAddress().getZip());

        return userDataResponse;
    }
}
