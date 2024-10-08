package amnil.internship.service.student;

import amnil.internship.dto.request.UpdateStudentRequest;
import amnil.internship.model.Student;

import java.util.List;

public interface IStudentService {
    Student registerStudent(Student student);

    List<Student> getAllStudent();

    Student getStudentById(Long id);

    void deleteStudent(Long roll);

    Student updateStudent(UpdateStudentRequest student, Long roll);


}
