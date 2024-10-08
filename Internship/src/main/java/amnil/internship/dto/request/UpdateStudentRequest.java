package amnil.internship.dto.request;

import amnil.internship.model.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateStudentRequest {
    private Long roll;

    private User user;

    private String course;
    private int semester;
    private LocalDate dob;


}


