package amnil.internship.dto.request;

import amnil.internship.enums.Role;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class SaveUserRequest {
    private Long id;
    private String fullname;
    private String email;
    private Role role;
    private String phoneNumber;

    private int semester;
    private LocalDate dob;
    private String course;

    private String username;
    private String password;

    private String city;
    private String state;
    private String zip;
    private String country;

}
