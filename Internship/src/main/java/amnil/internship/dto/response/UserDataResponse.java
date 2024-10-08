package amnil.internship.dto.response;

import amnil.internship.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class UserDataResponse {
    private Long id;
    private String fullname;
    private String email;
    private Role role;
    private String phoneNumber;

    private int semester;
    private LocalDate dob;
    private String course;

    private String username;

    private String city;
    private String state;
    private String country;
    private String zip;

}
